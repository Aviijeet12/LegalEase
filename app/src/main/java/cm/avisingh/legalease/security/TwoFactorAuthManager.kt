package cm.avisingh.legalease.security

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class TwoFactorAuthManager(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val securityManager: SecurityManager
) {
    private var verificationId: String? = null

    fun isEnabled(): Boolean {
        val user = auth.currentUser ?: return false
        return user.phoneNumber != null
    }

    fun startPhoneVerification(
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context as android.app.Activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    securityManager.logSecurityEvent(
                        "2FA_VERIFICATION_COMPLETED",
                        "Phone verification completed automatically"
                    )
                    onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                    securityManager.logSecurityEvent(
                        "2FA_VERIFICATION_FAILED",
                        "Phone verification failed: ${e.message}"
                    )
                    onVerificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@TwoFactorAuthManager.verificationId = verificationId
                    securityManager.logSecurityEvent(
                        "2FA_CODE_SENT",
                        "Verification code sent to $phoneNumber"
                    )
                    onCodeSent(verificationId)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyPhoneCode(
        code: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val verificationId = this.verificationId
        if (verificationId == null) {
            onError(IllegalStateException("Verification ID not found"))
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        auth.currentUser?.linkWithCredential(credential)
            ?.addOnSuccessListener {
                securityManager.logSecurityEvent(
                    "2FA_SETUP_COMPLETED",
                    "Two-factor authentication setup completed"
                )
                saveUserPreferences(true)
                onSuccess()
            }
            ?.addOnFailureListener { e ->
                securityManager.logSecurityEvent(
                    "2FA_SETUP_FAILED",
                    "Failed to set up two-factor authentication: ${e.message}"
                )
                onError(e)
            }
    }

    private fun saveUserPreferences(twoFactorEnabled: Boolean) {
        val user = auth.currentUser ?: return
        firestore.collection("user_preferences")
            .document(user.uid)
            .update("twoFactorEnabled", twoFactorEnabled)
    }
}