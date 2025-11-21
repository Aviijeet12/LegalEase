package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityClientProfileBinding

class ClientProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientProfileBinding
    private val mockKycDocument = "/mnt/data/le-z.zip"   // Using your uploaded file as mock doc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockData()
        setupClicks()
    }

    private fun loadMockData() {
        binding.tvName.text = "Avijit Pratap"
        binding.tvEmail.text = "avijit@example.com"
        binding.tvPhone.text = "+91 9876543210"

        binding.tvKycStatus.text = "Verified"
        binding.tvKycStatus.setTextColor(0xFF4CAF50.toInt())
    }

    private fun setupClicks() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.btnDownloadKyc.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(mockKycDocument), "*/*")
                startActivity(intent)
            } catch (_: Exception) {}
        }

        binding.btnLogout.setOnClickListener {
            finish()  // Later: clear auth
        }
    }
}

