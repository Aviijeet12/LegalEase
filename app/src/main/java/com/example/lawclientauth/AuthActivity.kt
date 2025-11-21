package com.example.lawclientauth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    private var isSignupMode = false
    private var selectedRole = "Client"   // Default role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupModeSwitch()
        setupRoleSelection()
    }

    // ------------------------------
    //  LOGIN / SIGNUP SWITCH
    // ------------------------------
    private fun setupModeSwitch() {

        binding.btnLogin.setOnClickListener {
            isSignupMode = false
            binding.tvTitle.text = "Login"
            binding.tilConfirmPassword.visibility = View.GONE
            binding.btnAction.text = "Login"

            // Button colors
            binding.btnLogin.setBackgroundColor(0xFF0B2F4E.toInt())
            binding.btnSignup.setBackgroundColor(0xFFD4AF37.toInt())
        }

        binding.btnSignup.setOnClickListener {
            isSignupMode = true
            binding.tvTitle.text = "Sign Up"
            binding.tilConfirmPassword.visibility = View.VISIBLE
            binding.btnAction.text = "Create Account"

            binding.btnSignup.setBackgroundColor(0xFF0B2F4E.toInt())
            binding.btnLogin.setBackgroundColor(0xFFD4AF37.toInt())
        }
    }

    // ------------------------------
    //  ROLE SELECTION FRONTEND
    // ------------------------------
    private fun setupRoleSelection() {

        binding.btnClient.setOnClickListener {
            selectedRole = "Client"

            // Visual feedback
            binding.btnClient.setBackgroundColor(0xFF0B2F4E.toInt())
            binding.btnLawyer.setBackgroundColor(0xFFD4AF37.toInt())
        }

        binding.btnLawyer.setOnClickListener {
            selectedRole = "Lawyer"

            binding.btnLawyer.setBackgroundColor(0xFF0B2F4E.toInt())
            binding.btnClient.setBackgroundColor(0xFFD4AF37.toInt())
        }
    }

    // ------------------------------
    //  BUTTON CLICK (Frontend only)
    // ------------------------------
    private fun handleActionClick() {

        binding.btnAction.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Email and password required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isSignupMode && pass != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // FRONTEND ONLY — backend later
            if (isSignupMode) {
                Toast.makeText(
                    this,
                    "Signup pressed • Role: $selectedRole",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Login pressed • Role: $selectedRole",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleActionClick()
    }
}
