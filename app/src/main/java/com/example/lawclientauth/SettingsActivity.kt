package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switchDark = findViewById<Switch>(R.id.switchDarkMode)
        val switchBio = findViewById<Switch>(R.id.switchBiometric)
        val btnPrivacy = findViewById<Button>(R.id.btnPrivacyPolicy)
        val btnTerms = findViewById<Button>(R.id.btnTerms)
        val btnAbout = findViewById<Button>(R.id.btnAbout)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnDelete = findViewById<Button>(R.id.btnDeleteAccount)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val dark = prefs.getBoolean("dark_mode", false)
        val bio = prefs.getBoolean("biometrics_enabled", false)

        switchDark.isChecked = dark
        switchBio.isChecked = bio

        switchDark.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES 
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        switchBio.setOnCheckedChangeListener { _, enabled ->
            prefs.edit().putBoolean("biometrics_enabled", enabled).apply()
            Toast.makeText(this, "Biometrics ${if (enabled) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
        }

        btnPrivacy.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://your-privacy-policy.com")))
        }

        btnTerms.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://your-terms-link.com")))
        }

        btnAbout.setOnClickListener {
            Toast.makeText(this, "LawClient App v1.0", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnDelete.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            user?.delete()?.addOnSuccessListener {
                startActivity(Intent(this, SignupActivity::class.java))
                finish()
            }
        }
    }
}
