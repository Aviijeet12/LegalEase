package com.nyayasetu.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private var isLoginMode = true

    // Account types
    private val accountTypes = listOf("Client", "Lawyer")
    private var selectedAccountType = "Client"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabSelection()
        setupAccountTypeSpinner()
        setupDemoAccess()
        setupGoogleSignIn()
    }

    private fun setupTabSelection() {
        binding.loginTab.setOnClickListener {
            switchToLoginMode()
        }

        binding.signupTab.setOnClickListener {
            switchToSignupMode()
        }
    }

    private fun switchToLoginMode() {
        isLoginMode = true
        updateTabSelection()
        showLoginFields()
    }

    private fun switchToSignupMode() {
        isLoginMode = false
        updateTabSelection()
        showSignupFields()
    }

    private fun updateTabSelection() {
        binding.loginTab.isSelected = isLoginMode
        binding.signupTab.isSelected = !isLoginMode

        binding.loginTab.setTextColor(
            if (isLoginMode) getColor(android.R.color.white) else getColor(android.R.color.darker_gray)
        )
        binding.signupTab.setTextColor(
            if (!isLoginMode) getColor(android.R.color.white) else getColor(android.R.color.darker_gray)
        )

        binding.loginButton.text = if (isLoginMode) "Login" else "Create Account"
    }

    private fun showLoginFields() {
        binding.nameLayout.visibility = android.view.View.GONE
        binding.confirmPasswordLayout.visibility = android.view.View.GONE
        binding.forgotPassword.visibility = android.view.View.VISIBLE
        binding.demoAccessLayout.visibility = android.view.View.VISIBLE
    }

    private fun showSignupFields() {
        binding.nameLayout.visibility = android.view.View.VISIBLE
        binding.confirmPasswordLayout.visibility = android.view.View.VISIBLE
        binding.forgotPassword.visibility = android.view.View.GONE
        binding.demoAccessLayout.visibility = android.view.View.GONE
    }

    private fun setupAccountTypeSpinner() {
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, accountTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.accountTypeSpinner.adapter = adapter

        binding.accountTypeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedAccountType = accountTypes[position]
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupDemoAccess() {
        binding.userDemo.setOnClickListener {
            fillDemoCredentials("user")
        }

        binding.lawyerDemo.setOnClickListener {
            fillDemoCredentials("lawyer")
        }

        binding.adminDemo.setOnClickListener {
            fillDemoCredentials("admin")
        }
    }

    private fun fillDemoCredentials(type: String) {
        when (type) {
            "user" -> {
                binding.accountTypeSpinner.setSelection(0) // Client
                binding.emailEditText.setText("user@demo.com")
                binding.passwordEditText.setText("password123")
            }
            "lawyer" -> {
                binding.accountTypeSpinner.setSelection(1) // Lawyer
                binding.emailEditText.setText("lawyer@demo.com")
                binding.passwordEditText.setText("password123")
            }
            "admin" -> {
                binding.accountTypeSpinner.setSelection(0) // Client
                binding.emailEditText.setText("admin@demo.com")
                binding.passwordEditText.setText("admin123")
            }
        }
    }

    private fun setupGoogleSignIn() {
        binding.googleSignUpButton.setOnClickListener {
            // Implement Google Sign-In logic here
            performGoogleSignIn()
        }
    }

    private fun performGoogleSignIn() {
        // TODO: Implement Google Sign-In integration
        android.widget.Toast.makeText(this, "Google Sign-Up clicked", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun validateForm(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailEditText.error = "Please enter email"
            return false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Please enter password"
            return false
        }

        if (!isLoginMode) {
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (firstName.isEmpty()) {
                binding.firstNameEditText.error = "Please enter first name"
                return false
            }

            if (lastName.isEmpty()) {
                binding.lastNameEditText.error = "Please enter last name"
                return false
            }

            if (confirmPassword.isEmpty()) {
                binding.confirmPasswordEditText.error = "Please confirm password"
                return false
            }

            if (password != confirmPassword) {
                binding.confirmPasswordEditText.error = "Passwords don't match"
                return false
            }
        }

        return true
    }

    private fun performLoginOrSignup() {
        if (validateForm()) {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val accountType = selectedAccountType

            // Save user preferences
            val prefs = PreferenceHelper(this)
            prefs.setLoggedIn(true)
            prefs.saveUserEmail(email)

            val intent = when {
                email.contains("admin") -> {
                    prefs.saveUserType("admin")
                    Intent(this, AdminMainActivity::class.java)
                }
                accountType == "Lawyer" -> {
                    prefs.saveUserType("lawyer")
                    Intent(this, LawyerMainActivity::class.java)
                }
                else -> {
                    prefs.saveUserType("client")
                    Intent(this, MainActivity::class.java)
                }
            }
            startActivity(intent)
            finish()
        }
    }
}