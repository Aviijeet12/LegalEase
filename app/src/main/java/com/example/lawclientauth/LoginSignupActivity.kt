package com.example.lawclientauth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityLoginSignupBinding

class LoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("LEGAL_EASE_PREFS", MODE_PRIVATE)

        setupRoleSelection()
        setupLoginButton()
        setupSignupButton()
    }

    private var selectedRole: String = "client"  // default role

    private fun setupRoleSelection() {
        binding.radioClient.setOnClickListener {
            selectedRole = "client"
        }

        binding.radioLawyer.setOnClickListener {
            selectedRole = "lawyer"
        }
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {

            val email = binding.etLoginEmail.text.toString().trim()
            val pass = binding.etLoginPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ---------------------------
            // MOCK LOGIN SUCCESS (Frontend only)
            // ---------------------------
            saveLoginStatus(selectedRole)
            redirectToDashboard(selectedRole)
        }
    }

    private fun setupSignupButton() {
        binding.btnSignup.setOnClickListener {

            val name = binding.etSignupName.text.toString().trim()
            val email = binding.etSignupEmail.text.toString().trim()
            val pass = binding.etSignupPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ---------------------------
            // MOCK SIGNUP SUCCESS (Frontend only)
            // ---------------------------
            saveLoginStatus(selectedRole)
            redirectToDashboard(selectedRole)
        }
    }

    private fun saveLoginStatus(role: String) {
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putString("userRole", role)
            .apply()
    }

    private fun redirectToDashboard(role: String) {
        if (role == "lawyer") {
            startActivity(Intent(this, LawyerDashboardActivity::class.java))
        } else {
            startActivity(Intent(this, ClientDashboardActivity::class.java))
        }
        finish()
    }
}
