package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.MessagingManager

class MessagingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        // Example: Subscribe to notifications/messages
        MessagingManager.subscribeToNotifications("messages") { success ->
            if (success) {
                Toast.makeText(this, "Subscribed to messages.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to subscribe.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
