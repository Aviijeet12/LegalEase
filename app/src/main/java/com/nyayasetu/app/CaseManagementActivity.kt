package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.FirestoreManager

class CaseManagementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_management)

        // Example: Load cases from Firestore
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView?>(R.id.recyclerViewCases)
        FirestoreManager.getCases { casesSnapshot ->
            if (casesSnapshot != null && recyclerView != null) {
                val caseList = casesSnapshot.documents.mapNotNull { it.data }
                recyclerView.adapter = CaseAdapter(caseList)
                recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
                Toast.makeText(this, "Loaded ${caseList.size} cases.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to load cases.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
