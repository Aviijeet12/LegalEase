package com.nyayasetu.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.databinding.ActivityLanguageSelectionBinding

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding
    private var selectedLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLanguageList()
        setupContinueButton()
    }

    private fun setupLanguageList() {
        val languages = listOf(
            Language("IN English", "English"),
            Language("IN feed (Hindi)", "Hindi"),
            Language("IN green (Bengali)", "Bengali"),
            Language("IN gecx6 (Telugu)", "Telugu"),
            Language("IN HRIO (Marathi)", "Marathi"),
            Language("IN goldip (Tamil)", "Tamil"),
            Language("IN special (Gujarati)", "Gujarati"),
            Language("IN gidljp (Tamil)", "Tamil"),
            Language("IN yvxtl (Gujarati)", "Gujarati"),
            Language("IN vxjz (Kannada)", "Kannada"),
            Language("IN 0D1009o (Malayalam)", "Malayalam"),
            Language("IN rnrj (Punjabi)", "Punjabi"),
            Language("IN o@ll (Odia)", "Odia"),
            Language("IN vyrj (Assamese)", "Assamese")
        )

        val adapter = LanguageAdapter(languages) { language ->
            selectedLanguage = language.name
            // Update UI to show selection
            updateSelection()
        }

        binding.languageRecyclerView.adapter = adapter
    }

    private fun setupContinueButton() {
        binding.continueButton.setOnClickListener {
            if (selectedLanguage != null) {
                // Navigate to Auth Activity instead of Main Activity
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                android.widget.Toast.makeText(this, "Please select a language", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSelection() {
        binding.continueButton.isEnabled = selectedLanguage != null
    }
}

data class Language(val code: String, val name: String)