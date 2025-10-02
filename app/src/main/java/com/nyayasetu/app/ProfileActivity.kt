package com.nyayasetu.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.AuthManager
import com.nyayasetu.app.firebase.FirestoreManager

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val saveButton = findViewById<Button>(R.id.buttonSave)

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = AuthManager.getCurrentUser()
            if (user != null) {
                val data = mapOf("name" to name)
                FirestoreManager.setUserProfile(user.uid, data) { success ->
                    if (success) {
                        Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save profile. Please check your connection and try again.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
