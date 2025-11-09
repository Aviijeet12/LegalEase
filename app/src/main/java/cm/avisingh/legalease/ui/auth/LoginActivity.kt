package cm.avisingh.legalease.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityLoginBinding
import cm.avisingh.legalease.utils.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var firestore: FirebaseFirestore

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.e("LoginActivity", "Google sign in failed", e)
            val errorMsg = when (e.statusCode) {
                10 -> "Google Sign-In not configured. Please use Email/Password login or contact support."
                12501 -> "Sign-in cancelled"
                else -> "Google sign in failed: ${e.message}"
            }
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            setLoading(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "=== LOGIN ACTIVITY STARTED ===")
        
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("LoginActivity", "Layout inflated successfully")

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        sharedPrefManager = SharedPrefManager(this)
        
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        
        Log.d("LoginActivity", "Firebase auth initialized")

        // Check if Google Sign-In is configured
        checkGoogleSignInConfiguration()
        
        setupInputValidation()
        setupClickListeners()
        
        Log.d("LoginActivity", "onCreate completed successfully")
    }    private fun checkGoogleSignInConfiguration() {
        val webClientId = getString(R.string.default_web_client_id)
        if (webClientId.contains("YOUR_ACTUAL_CLIENT_ID")) {
            // Disable Google Sign-In button and show why
            binding.btnGoogleSignIn.alpha = 0.5f
            Log.w("LoginActivity", "Google Sign-In not configured - placeholder client ID detected")
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
        binding.btnGoogleSignIn.setOnClickListener { 
            // Check if Google Sign-In is properly configured
            val webClientId = getString(R.string.default_web_client_id)
            if (webClientId.contains("YOUR_ACTUAL_CLIENT_ID")) {
                showGoogleSignInNotConfiguredDialog()
            } else {
                signInWithGoogle()
            }
        }
        binding.tvSignUp.setOnClickListener { openRegister() }
        binding.tvForgotPassword.setOnClickListener { showForgotPasswordDialog() }
    }
    
    private fun showGoogleSignInNotConfiguredDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Google Sign-In Not Available")
            .setMessage("Google Sign-In is not configured yet. Please use Email/Password to sign in or create an account.\n\nTo enable Google Sign-In:\n1. Go to Firebase Console\n2. Enable Google Sign-In authentication\n3. Add your app's SHA-1 fingerprint\n4. Update the app configuration")
            .setPositiveButton("Use Email/Password") { _, _ ->
                // Focus on email field
                binding.etEmail.requestFocus()
            }
            .setNegativeButton("Create Account") { _, _ ->
                openRegister()
            }
            .show()
    }
    
    private fun signInWithGoogle() {
        setLoading(true)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
    
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle: ${account.id}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "signInWithCredential:success")
                    val user = auth.currentUser
                    
                    // Save user data and show role selection
                    user?.let {
                        checkAndSaveUserData(it.uid, it.displayName ?: "", it.email ?: "")
                    }
                } else {
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", 
                        Toast.LENGTH_SHORT).show()
                    setLoading(false)
                }
            }
    }
    
    private fun checkAndSaveUserData(userId: String, name: String, email: String) {
        // Check if user exists in Firestore
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // User exists, check if they have a valid role
                    val role = document.getString("role")
                    Log.d("LoginActivity", "Existing user found. Role from Firestore: '$role'")
                    
                    // If role is null or empty, force role selection
                    if (role.isNullOrEmpty()) {
                        Log.d("LoginActivity", "Role is null or empty, showing role selection")
                        setLoading(false)
                        showRoleSelectionDialog(userId, name, email)
                    } else {
                        Log.d("LoginActivity", "Valid role found: $role")
                        saveUserDataAndNavigate(userId, name, email, role)
                    }
                } else {
                    // New user, show role selection dialog
                    Log.d("LoginActivity", "New user, showing role selection")
                    setLoading(false)
                    showRoleSelectionDialog(userId, name, email)
                }
            }
            .addOnFailureListener { e ->
                Log.e("LoginActivity", "Error checking user", e)
                setLoading(false)
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    
    private fun showRoleSelectionDialog(userId: String, name: String, email: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Your Role")
            .setMessage("Are you a lawyer or a client?")
            .setPositiveButton("Lawyer") { _, _ ->
                Log.d("LoginActivity", "User clicked LAWYER button")
                Toast.makeText(this, "Saving as Lawyer...", Toast.LENGTH_SHORT).show()
                saveUserToFirestore(userId, name, email, "lawyer")
            }
            .setNegativeButton("Client") { _, _ ->
                Log.d("LoginActivity", "User clicked CLIENT button")
                Toast.makeText(this, "Saving as Client...", Toast.LENGTH_SHORT).show()
                saveUserToFirestore(userId, name, email, "client")
            }
            .setCancelable(false)
            .show()
    }
    
    private fun saveUserToFirestore(userId: String, name: String, email: String, role: String) {
        Log.d("LoginActivity", "=== SAVING USER WITH ROLE: '$role' ===")
        
        // IMPORTANT: Save to SharedPreferences FIRST to ensure MainActivity has the role
        sharedPrefManager.setUserId(userId)
        sharedPrefManager.setUserName(name)
        sharedPrefManager.setUserEmail(email)
        sharedPrefManager.setUserRole(role)
        sharedPrefManager.setIsLoggedIn(true)
        
        Log.d("LoginActivity", "All user data saved to SharedPreferences")
        
        Log.d("LoginActivity", "Saved to SharedPreferences. Verifying...")
        val savedRole = sharedPrefManager.getUserRole()
        Log.d("LoginActivity", "Verified role in SharedPreferences: '$savedRole'")
        
        setLoading(true)
        
        val userData = hashMapOf(
            "name" to name,
            "email" to email,
            "role" to role,
            "createdAt" to System.currentTimeMillis(),
            "updatedAt" to System.currentTimeMillis()
        )
        
        // Save to Firestore in background (don't wait for it)
        firestore.collection("users").document(userId)
            .set(userData, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("LoginActivity", "User data saved successfully to Firestore")
                    setLoading(false)
                    // Pass the role in the intent to avoid any timing/race issues when MainActivity reads SharedPreferences
                    startMainActivity(role)
                }
                .addOnFailureListener { e ->
                    Log.e("LoginActivity", "Error saving user data to Firestore", e)
                    setLoading(false)
                    // Still navigate even if Firestore fails, since we have local data
                    Toast.makeText(this, "Saved locally, proceeding...", Toast.LENGTH_SHORT).show()
                    startMainActivity(role)
                }
    }
    
    private fun saveUserDataAndNavigate(userId: String, name: String, email: String, role: String) {
        Log.d("LoginActivity", "Existing user - Saving to SharedPreferences. Role: $role")
        sharedPrefManager.setUserId(userId)
        sharedPrefManager.setUserName(name)
        sharedPrefManager.setUserEmail(email)
        sharedPrefManager.setUserRole(role)
        sharedPrefManager.setIsLoggedIn(true)
        
        // Verify role was saved
        val savedRole = sharedPrefManager.getUserRole()
        Log.d("LoginActivity", "Verified saved role: '$savedRole'")
        Toast.makeText(this, "Welcome back! Role: $role", Toast.LENGTH_SHORT).show()
        
        setLoading(false)
        // Pass role via intent for MainActivity to pick up immediately
        startMainActivity(role)
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
                if (task.isSuccessful) {
                    // Login successful - check user data and role
                    Log.d("LoginActivity", "Email/Password sign-in successful")
                    val user = auth.currentUser
                    user?.let {
                        checkAndSaveUserData(it.uid, it.displayName ?: "", it.email ?: "")
                    }
                } else {
                    // Login failed
                    setLoading(false)
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

    // New overload that includes role to be delivered via intent extras. This ensures MainActivity
    // can use the role immediately even if SharedPreferences hasn't been read elsewhere yet.
    private fun startMainActivity(role: String?) {
        Log.d("LoginActivity", "=== STARTING MAIN ACTIVITY WITH ROLE: '$role' ===")
        val intent = Intent(this, cm.avisingh.legalease.activities.MainActivity::class.java)
        if (!role.isNullOrEmpty()) {
            intent.putExtra("user_role", role)
            Log.d("LoginActivity", "Added 'user_role' extra to intent: '$role'")
        } else {
            Log.w("LoginActivity", "Role is null or empty, not adding to intent!")
        }
        startActivity(intent)
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