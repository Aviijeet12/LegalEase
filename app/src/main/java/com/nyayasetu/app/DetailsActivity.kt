package com.nyayasetu.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        // TODO: Load details from Firestore or API
        Toast.makeText(this, "Details loaded.", Toast.LENGTH_SHORT).show()
    }
}
