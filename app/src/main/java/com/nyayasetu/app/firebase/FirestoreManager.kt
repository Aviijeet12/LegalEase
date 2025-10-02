package com.nyayasetu.app.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Task

object FirestoreManager {
    private val db = FirebaseFirestore.getInstance()

    fun getUserProfile(uid: String, onResult: (Map<String, Any>?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                onResult(document.data)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun setUserProfile(uid: String, data: Map<String, Any>, onResult: (Boolean) -> Unit) {
        db.collection("users").document(uid).set(data)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getCases(onResult: (QuerySnapshot?) -> Unit) {
        db.collection("cases").get()
            .addOnSuccessListener { result -> onResult(result) }
            .addOnFailureListener { onResult(null) }
    }

    // Add more Firestore methods as needed for lawyers, admins, messages, notifications
}
