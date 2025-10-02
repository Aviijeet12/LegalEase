package com.nyayasetu.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        // TODO: Load user data from Firestore and display dashboard

        val logoutButton = findViewById<android.widget.Button?>(R.id.buttonLogout)
        logoutButton?.setOnClickListener {
            // TODO: Implement logout functionality
            val intent = android.content.Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
