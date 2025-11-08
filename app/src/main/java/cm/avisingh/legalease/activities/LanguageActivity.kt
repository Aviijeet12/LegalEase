package cm.avisingh.legalease.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.utils.AnalyticsHelper

class LanguageActivity : AppCompatActivity() {

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var btnContinue: Button
    private lateinit var tvSelectedLanguage: TextView

    private val languages = listOf(
        "English", "Hindi", "Bengali", "Telugu", "Marathi",
        "Tamil", "Gujarati", "Kannada", "Malayalam", "Punjabi", "Odia", "Assamese"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        analyticsHelper.trackScreen(this::class.java.simpleName)

        initializeViews()
        setupClickListeners()
        setupLanguageList()
    }

    private fun initializeViews() {
        btnContinue = findViewById(R.id.btnContinue)
        tvSelectedLanguage = findViewById(R.id.tvSelectedLanguage)
    }

    private fun setupClickListeners() {
        btnContinue.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    private fun setupLanguageList() {
        // For demo, select English by default
        tvSelectedLanguage.text = "English"
    }
}