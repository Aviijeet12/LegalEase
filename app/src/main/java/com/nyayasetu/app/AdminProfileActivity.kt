package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.FirestoreManager

class AdminProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)
        // TODO: Load admin profile from Firestore
        val adminId = "ADMIN_ID" // Replace with actual logic
        FirestoreManager.getUserProfile(adminId) { profile ->
            if (profile != null) {
                Toast.makeText(this, "Admin profile loaded.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to load admin profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
