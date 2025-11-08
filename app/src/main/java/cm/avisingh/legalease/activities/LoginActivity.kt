package cm.avisingh.legalease.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.LegalEaseApplication
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityLoginBinding
import cm.avisingh.legalease.services.AuthService
import cm.avisingh.legalease.utils.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private val authService = AuthService()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager(this)
        trackScreen()
        setupClickListeners()
    }

    private fun trackScreen() {
        val analyticsHelper = LegalEaseApplication.analyticsHelper
        analyticsHelper.trackScreenView("LoginActivity")
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }

        binding.tvSignUp.setOnClickListener {
            Toast.makeText(this, getString(R.string.signup_coming_soon), Toast.LENGTH_SHORT).show()
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, getString(R.string.password_reset_coming_soon), Toast.LENGTH_SHORT).show()
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val role = if (binding.radioLawyer.isChecked) "lawyer" else "client"

        if (validateInputs(email, password)) {
            performLogin(email, password, role)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.email_required)
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = getString(R.string.valid_email_required)
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.password_required)
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = getString(R.string.password_min_length)
            return false
        }

        return true
    }

    private fun performLogin(email: String, password: String, role: String) {
        binding.btnLogin.text = getString(R.string.signing_in)
        binding.btnLogin.isEnabled = false

        coroutineScope.launch {
            try {
                val result = authService.loginUser(email, password)
                when (result) {
                    is AuthService.Result.Success -> {
                        val user = result.user
                        loginSuccess(user.email ?: email, role, user.uid)
                    }
                    is AuthService.Result.Failure -> {
                        loginFailed(result.errorMessage)
                    }
                }
            } catch (e: Exception) {
                loginFailed(getString(R.string.login_error, e.message))
            }
        }
    }

    private fun loginSuccess(email: String, role: String, userId: String) {
        sharedPrefManager.setIsLoggedIn(true)
        sharedPrefManager.setUserEmail(email)
        sharedPrefManager.setUserRole(role)
        sharedPrefManager.setUserName(authService.getCurrentUser()?.displayName ?: getString(R.string.default_user_name))
        sharedPrefManager.setUserId(userId)

        val analyticsHelper = LegalEaseApplication.analyticsHelper
        analyticsHelper.trackEvent("user_login", mapOf(
            "user_role" to role,
            "login_method" to "email"
        ))

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

        Toast.makeText(this, getString(R.string.welcome_message), Toast.LENGTH_SHORT).show()
    }

    private fun loginFailed(errorMessage: String) {
        binding.btnLogin.text = getString(R.string.sign_in)
        binding.btnLogin.isEnabled = true
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

        val analyticsHelper = LegalEaseApplication.analyticsHelper
        analyticsHelper.logEvent("login_error", androidx.core.os.bundleOf(
            "error_message" to errorMessage,
            "error_category" to "Authentication"
        ))
    }
}