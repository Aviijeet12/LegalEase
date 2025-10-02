package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.FirestoreManager

class LawyerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lawyer_profile)
        // TODO: Load lawyer profile from Firestore
        val lawyerId = "LAWYER_ID" // Replace with actual logic
        FirestoreManager.getUserProfile(lawyerId) { profile ->
            if (profile != null) {
                Toast.makeText(this, "Lawyer profile loaded.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to load lawyer profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
