package cm.avisingh.legalease.security

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SecureDocumentManager(
    private val context: Context,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val encryptionManager: EncryptionManager,
    private val securityManager: SecurityManager
) {
    fun encryptAndUploadDocument(
        localUri: Uri,
        remotePath: String,
        onProgress: (Int) -> Unit,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val file = File(localUri.path!!)
            val encryptedFile = encryptFile(file)
            
            val storageRef = storage.reference.child(remotePath)
            val uploadTask = storageRef.putFile(Uri.fromFile(encryptedFile))

            uploadTask
                .addOnProgressListener { snapshot ->
                    val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
                    onProgress(progress)
                }
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        logDocumentAccess("UPLOAD", remotePath)
                        encryptedFile.delete()
                        onSuccess(downloadUrl.toString())
                    }
                }
                .addOnFailureListener { e ->
                    encryptedFile.delete()
                    onError(e)
                }
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun downloadAndDecryptDocument(
        remotePath: String,
        localPath: String,
        onProgress: (Int) -> Unit,
        onSuccess: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val storageRef = storage.reference.child(remotePath)
        val encryptedFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}")
        
        storageRef.getFile(encryptedFile)
            .addOnProgressListener { snapshot ->
                val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
                onProgress(progress)
            }
            .addOnSuccessListener {
                try {
                    val decryptedFile = decryptFile(encryptedFile, localPath)
                    logDocumentAccess("DOWNLOAD", remotePath)
                    encryptedFile.delete()
                    onSuccess(decryptedFile)
                } catch (e: Exception) {
                    encryptedFile.delete()
                    onError(e)
                }
            }
            .addOnFailureListener { e ->
                encryptedFile.delete()
                onError(e)
            }
    }

    private fun encryptFile(file: File): File {
        val encryptedFile = File(context.cacheDir, "encrypted_${file.name}")
        val fileData = file.readBytes()
        // TODO: Implement file encryption with proper binary handling
        // For now, just copy the file (encryption disabled temporarily)
        encryptedFile.writeBytes(fileData)
        return encryptedFile
    }

    private fun decryptFile(encryptedFile: File, outputPath: String): File {
        val decryptedFile = File(outputPath)
        val encryptedData = encryptedFile.readBytes()
        // TODO: Implement file decryption with proper binary handling
        // For now, just copy the file (decryption disabled temporarily)
        decryptedFile.writeBytes(encryptedData)
        return decryptedFile
    }

    private fun logDocumentAccess(action: String, documentPath: String) {
        val userId = auth.currentUser?.uid ?: return
        
        securityManager.logSecurityEvent(
            "DOCUMENT_$action",
            "Document $action: $documentPath"
        )

        // Schedule background worker to sync access logs
        val inputData = Data.Builder()
            .putString("userId", userId)
            .putString("action", action)
            .putString("documentPath", documentPath)
            .putLong("timestamp", System.currentTimeMillis())
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<AccessLogWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }
}