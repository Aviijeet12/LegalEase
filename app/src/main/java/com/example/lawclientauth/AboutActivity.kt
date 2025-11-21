package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvContent.text = """
            Legal Ease Application
            Version: 1.0.0

            Legal Ease connects clients with certified lawyers.
            Features:
            - Lawyer search & matching
            - Case management
            - Appointment scheduling
            - Chat & video call
            - Secure document upload

            More features coming soon!
        """.trimIndent()
    }
}
