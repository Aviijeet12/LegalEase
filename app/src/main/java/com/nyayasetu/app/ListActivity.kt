package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.FirestoreManager

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        // TODO: Load list data from Firestore
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView?>(R.id.recyclerViewList)
        FirestoreManager.getCases { casesSnapshot ->
            if (casesSnapshot != null && recyclerView != null) {
                val itemList = casesSnapshot.documents.mapNotNull { it.data }
                recyclerView.adapter = CaseAdapter(itemList)
                recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
                Toast.makeText(this, "Loaded ${itemList.size} items.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to load list.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
