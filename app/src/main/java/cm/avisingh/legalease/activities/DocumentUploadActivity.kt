package cm.avisingh.legalease.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.databinding.ActivityDocumentUploadBinding
import cm.avisingh.legalease.utils.SharedPrefManager
import java.io.File
import cm.avisingh.legalease.utils.AnalyticsHelper

class DocumentUploadActivity : AppCompatActivity() {

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var binding: ActivityDocumentUploadBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private var selectedFileUri: Uri? = null

    // Document types
    private val documentTypes = arrayOf(
        "Legal Brief",
        "Contract Agreement",
        "Court Filing",
        "Evidence Document",
        "Client Correspondence",
        "Legal Research",
        "Other"
    )

    // Mock cases for lawyers
    private val mockCases = arrayOf(
        "Smith vs. Johnson - Civil Case #CV-2024-001",
        "State vs. Davis - Criminal Case #CR-2024-015",
        "Miller Property Dispute - Case #PRO-2024-008",
        "Corporate Merger - Case #CORP-2024-003"
    )

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleSelectedFile(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analyticsHelper.trackScreen(this::class.java.simpleName)

        sharedPrefManager = SharedPrefManager(this)
        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        // Setup document type spinner
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, documentTypes)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDocumentType.adapter = typeAdapter

        // Show case selection only for lawyers
        if (sharedPrefManager.getUserRole() == "lawyer") {
            binding.layoutCaseSelection.visibility = android.view.View.VISIBLE
            val caseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mockCases)
            caseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCaseSelection.adapter = caseAdapter
        }
    }

    private fun setupClickListeners() {
        // Upload area click
        binding.layoutUploadArea.setOnClickListener {
            openFilePicker()
        }

        // Remove file button
        binding.ivRemoveFile.setOnClickListener {
            clearSelectedFile()
        }

        // Upload button
        binding.btnUpload.setOnClickListener {
            uploadDocument()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            filePickerLauncher.launch(Intent.createChooser(intent, "Select Document"))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "Please install a file manager", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSelectedFile(uri: Uri) {
        selectedFileUri = uri

        // Get file info
        val fileName = getFileName(uri)
        val fileSize = getFileSize(uri)

        // Update UI
        binding.tvFileName.text = fileName
        binding.tvFileSize.text = fileSize
        binding.layoutSelectedFile.visibility = android.view.View.VISIBLE
        binding.btnUpload.isEnabled = true

        // Show success message
        Toast.makeText(this, "File selected: $fileName", Toast.LENGTH_SHORT).show()
    }

    private fun getFileName(uri: Uri): String {
        var result = "Unknown File"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndexOrThrow(android.provider.OpenableColumns.DISPLAY_NAME))
                if (displayName != null) {
                    result = displayName
                }
            }
        }
        return result
    }

    private fun getFileSize(uri: Uri): String {
        var size = 0L
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                size = it.getLong(it.getColumnIndexOrThrow(android.provider.OpenableColumns.SIZE))
            }
        }

        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            else -> "${size / (1024 * 1024)} MB"
        }
    }

    private fun clearSelectedFile() {
        selectedFileUri = null
        binding.layoutSelectedFile.visibility = android.view.View.GONE
        binding.btnUpload.isEnabled = false
        Toast.makeText(this, "File removed", Toast.LENGTH_SHORT).show()
    }

    private fun uploadDocument() {
        val documentType = binding.spinnerDocumentType.selectedItem.toString()
        val title = binding.etDocumentTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (title.isEmpty()) {
            binding.etDocumentTitle.error = "Please enter a document title"
            return
        }

        if (selectedFileUri == null) {
            Toast.makeText(this, "Please select a file to upload", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading state
        binding.btnUpload.text = "Uploading..."
        binding.btnUpload.isEnabled = false

        // Simulate upload process
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            // Mock upload success
            uploadSuccess()
        }, 2000)
    }

    private fun uploadSuccess() {
        analyticsHelper.trackEvent("document_uploaded", mapOf(
            "file_type" to "pdf",
            "upload_success" to "true"
        ))
        binding.btnUpload.text = "Upload Document"
        binding.btnUpload.isEnabled = true
        analyticsHelper.logDocumentUploaded("PDF", 2048L, "case_123")
        analyticsHelper.logFeatureUsed("Document Upload")
        // Show success dialog
        android.app.AlertDialog.Builder(this)
            .setTitle("Upload Successful!")
            .setMessage("Your document has been uploaded securely and is now available for review.")
            .setPositiveButton("Great!") { dialog, which ->
                finish()
            }
            .setCancelable(false)
            .show()

        Toast.makeText(this, "Document uploaded successfully!", Toast.LENGTH_LONG).show()
    }
}