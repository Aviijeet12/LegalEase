package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // TODO: Implement settings logic (e.g., notification toggle, dark mode)
        Toast.makeText(this, "Settings loaded.", Toast.LENGTH_SHORT).show()
    }
}
