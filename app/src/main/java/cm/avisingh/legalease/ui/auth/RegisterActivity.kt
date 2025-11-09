package cm.avisingh.legalease.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import cm.avisingh.legalease.activities.MainActivity
import cm.avisingh.legalease.databinding.ActivityRegisterBinding
import cm.avisingh.legalease.utils.SharedPrefManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        sharedPrefManager = SharedPrefManager(this)

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
                                // Show role selection for new user
                                val user = auth.currentUser
                                user?.let {
                                    showRoleSelectionDialog(it.uid, name, email)
                                }
                            } else {
                                // Profile update failed but account created
                                Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
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

    private fun showRoleSelectionDialog(userId: String, name: String, email: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Your Role")
            .setMessage("Are you a lawyer or a client?")
            .setPositiveButton("Lawyer") { _, _ ->
                Log.d("RegisterActivity", "User selected LAWYER")
                saveUserToFirestore(userId, name, email, "lawyer")
            }
            .setNegativeButton("Client") { _, _ ->
                Log.d("RegisterActivity", "User selected CLIENT")
                saveUserToFirestore(userId, name, email, "client")
            }
            .setCancelable(false)
            .show()
    }

    private fun saveUserToFirestore(userId: String, name: String, email: String, role: String) {
        Log.d("RegisterActivity", "=== SAVING NEW USER WITH ROLE: '$role' ===")
        
        // Save to SharedPreferences FIRST
        sharedPrefManager.setUserId(userId)
        sharedPrefManager.setUserName(name)
        sharedPrefManager.setUserEmail(email)
        sharedPrefManager.setUserRole(role)
        sharedPrefManager.setIsLoggedIn(true)
        
        setLoading(true)
        
        val userData = hashMapOf(
            "name" to name,
            "email" to email,
            "role" to role,
            "createdAt" to System.currentTimeMillis(),
            "updatedAt" to System.currentTimeMillis()
        )
        
        firestore.collection("users").document(userId)
            .set(userData, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User data saved to Firestore")
                setLoading(false)
                startMainActivity(role)
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Error saving user data", e)
                setLoading(false)
                // Still navigate with local data
                startMainActivity(role)
            }
    }

    private fun startMainActivity(role: String) {
        Log.d("RegisterActivity", "=== STARTING MAIN ACTIVITY WITH ROLE: '$role' ===")
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_role", role)
        startActivity(intent)
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