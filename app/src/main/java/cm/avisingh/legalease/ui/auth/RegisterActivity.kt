package cm.avisingh.legalease.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import cm.avisingh.legalease.MainActivity
import cm.avisingh.legalease.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setupInputValidation()
        setupClickListeners()
    }

    private fun setupInputValidation() {
        binding.apply {
            // Clear errors on text change
            nameEditText.doAfterTextChanged {
                nameInputLayout.error = null
            }
            emailEditText.doAfterTextChanged {
                emailInputLayout.error = null
            }
            passwordEditText.doAfterTextChanged {
                passwordInputLayout.error = null
            }
            confirmPasswordEditText.doAfterTextChanged {
                confirmPasswordInputLayout.error = null
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            backButton.setOnClickListener { finish() }
            registerButton.setOnClickListener { attemptRegister() }
        }
    }

    private fun attemptRegister() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.confirmPasswordEditText.text.toString()

        // Validate input
        if (!validateInput(name, email, password, confirmPassword)) return

        // Show progress
        setLoading(true)

        // Create account
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Update display name
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }
                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            setLoading(false)
                            if (profileTask.isSuccessful) {
                                // Send verification email
                                sendVerificationEmail()
                            } else {
                                // Profile update failed but account created
                                startMainActivity()
                            }
                        }
                } else {
                    // Registration failed
                    setLoading(false)
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true
        binding.apply {
            if (name.isEmpty()) {
                nameInputLayout.error = "Name is required"
                isValid = false
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInputLayout.error = "Enter a valid email address"
                isValid = false
            }

            if (password.isEmpty() || password.length < 6) {
                passwordInputLayout.error = "Password must be at least 6 characters"
                isValid = false
            }

            if (confirmPassword != password) {
                confirmPasswordInputLayout.error = "Passwords do not match"
                isValid = false
            }
        }
        return isValid
    }

    private fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification email sent. Please check your inbox.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                startMainActivity()
            }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun setLoading(loading: Boolean) {
        binding.apply {
            progressIndicator.visibility = if (loading) View.VISIBLE else View.GONE
            registerButton.isEnabled = !loading
            backButton.isEnabled = !loading
            nameInputLayout.isEnabled = !loading
            emailInputLayout.isEnabled = !loading
            passwordInputLayout.isEnabled = !loading
            confirmPasswordInputLayout.isEnabled = !loading
        }
    }
}