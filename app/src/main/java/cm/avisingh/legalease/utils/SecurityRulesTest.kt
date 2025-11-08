package cm.avisingh.legalease.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SecurityRulesTest {

    fun testFirestoreRules() {
        val db = FirebaseFirestore.getInstance()
        val testUserId = "test_user_123"

        // Test 1: User should NOT be able to read other user's data
        db.collection("users").document("other_user_id").get()
            .addOnSuccessListener {
                println("SECURITY BREACH: User accessed other user's data!")
            }
            .addOnFailureListener {
                println("SECURITY PASS: User correctly blocked from other user's data")
            }

        // Test 2: User should be able to read their own data
        db.collection("users").document(testUserId).get()
            .addOnSuccessListener {
                println("SECURITY PASS: User accessed their own data")
            }
            .addOnFailureListener {
                println("SECURITY ISSUE: User blocked from their own data")
            }
    }

    fun testStorageRules() {
        val storage = FirebaseStorage.getInstance()

        // Test: User should NOT access other user's documents
        storage.reference.child("case_documents/other_case/document.pdf").downloadUrl
            .addOnSuccessListener {
                println("SECURITY BREACH: User accessed other case documents!")
            }
            .addOnFailureListener {
                println("SECURITY PASS: User correctly blocked from other case documents")
            }
    }
}