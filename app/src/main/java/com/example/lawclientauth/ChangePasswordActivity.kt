package com.example.lawclientauth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpdatePassword.setOnClickListener {
            val oldPass = binding.etOldPass.text.toString()
            val newPass = binding.etNewPass.text.toString()
            val confirmPass = binding.etConfirmPass.text.toString()

            if (newPass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Password updated (mock)", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
