package cm.avisingh.legalease.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import cm.avisingh.legalease.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setupInputValidation()
        setupClickListeners()

        // Check if user is already signed in
        auth.currentUser?.let {
            startMainActivity()
        }
    }

    private fun setupInputValidation() {
        // View binding with actual IDs from XML
        binding.etEmail.doAfterTextChanged {
            // Clear any previous errors
        }
        binding.etPassword.doAfterTextChanged {
            // Clear any previous errors
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.tvSignUp.setOnClickListener { openRegister() }
        binding.tvForgotPassword.setOnClickListener { showForgotPasswordDialog() }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        // Validate input
        if (!validateInput(email, password)) return

        // Show progress
        setLoading(true)

        // Attempt login
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                setLoading(false)
                if (task.isSuccessful) {
                    // Login successful
                    startMainActivity()
                } else {
                    // Login failed
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true
        
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (password.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        
        return isValid
    }

    private fun showForgotPasswordDialog() {
        val email = binding.etEmail.text.toString()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address first", Toast.LENGTH_SHORT).show()
            return
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Reset Password")
            .setMessage("Send password reset email to $email?")
            .setPositiveButton("Send") { _, _ ->
                sendPasswordResetEmail(email)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun sendPasswordResetEmail(email: String) {
        setLoading(true)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                setLoading(false)
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password reset email sent to $email",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send reset email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun openRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun startMainActivity() {
        startActivity(Intent(this, cm.avisingh.legalease.activities.MainActivity::class.java))
        finish()
    }

    private fun setLoading(loading: Boolean) {
        // TODO: Add progress indicator to layout
        binding.btnLogin.isEnabled = !loading
        binding.tvSignUp.isEnabled = !loading
        binding.tvForgotPassword.isEnabled = !loading
        binding.etEmail.isEnabled = !loading
        binding.etPassword.isEnabled = !loading
    }
}