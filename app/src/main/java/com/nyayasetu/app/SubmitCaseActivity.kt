package com.nyayasetu.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.databinding.ActivitySubmitCaseBinding

class SubmitCaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmitCaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupForm()
    }

    private fun setupForm() {
        binding.submitButton.setOnClickListener {
            if (validateForm()) {
                submitCase()
            }
        }

        binding.uploadDocuments.setOnClickListener {
            // Implement document upload
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.caseTitleInput.text.toString().trim().isEmpty()) {
            binding.caseTitleInput.error = "Please enter case title"
            isValid = false
        }

        if (binding.caseDescriptionInput.text.toString().trim().isEmpty()) {
            binding.caseDescriptionInput.error = "Please enter case description"
            isValid = false
        }

        if (binding.categorySpinner.selectedItemPosition == 0) {
            // Show error for category not selected
            isValid = false
        }

        return isValid
    }

    private fun submitCase() {
        // Submit case logic
        finish()
    }
}