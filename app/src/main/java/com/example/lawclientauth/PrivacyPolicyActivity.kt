package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvContent.text = """
            Privacy Policy

            This is a placeholder privacy policy for the Legal Ease application.
            The full version will be shown here when backend & policies are prepared.

            1. Data protection
            2. Encryption policy
            3. User rights
            4. Responsibility & usage guidelines
            5. Permissions required
            ...
        """.trimIndent()
    }
}
