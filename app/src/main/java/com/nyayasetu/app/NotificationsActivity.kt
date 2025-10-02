package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.MessagingManager

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Example: Subscribe to notifications
        MessagingManager.subscribeToNotifications("notifications") { success ->
            if (success) {
                Toast.makeText(this, "Subscribed to notifications.", Toast.LENGTH_SHORT).show()
                // TODO: Handle incoming push notifications and display them in the UI
            } else {
                Toast.makeText(this, "Failed to subscribe.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
