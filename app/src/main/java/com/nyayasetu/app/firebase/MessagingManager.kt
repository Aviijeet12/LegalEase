package com.nyayasetu.app.firebase

import com.google.firebase.messaging.FirebaseMessaging

object MessagingManager {
    fun subscribeToNotifications(topic: String, onResult: (Boolean) -> Unit) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }
}
