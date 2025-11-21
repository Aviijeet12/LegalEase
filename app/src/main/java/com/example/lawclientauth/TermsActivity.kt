package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvContent.text = """
            Terms & Conditions

            This is a placeholder for Legal Ease terms & conditions.
            The complete legal document will be added later.

            1. User Agreement
            2. Lawyer responsibilities
            3. Client obligations
            4. Payment guidelines
            5. Limitations
            ...
        """.trimIndent()
    }
}
