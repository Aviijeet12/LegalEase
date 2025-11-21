package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityDocumentListBinding

class DocumentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()

        binding.btnUploadDoc.setOnClickListener {
            startActivity(Intent(this, UploadDocumentActivity::class.java))
        }
    }

    private fun setupRecycler() {
        val docs = listOf(
            DocumentModel("FIR Report", "PDF", "12 Jan 2025", "/mnt/data/le-z.zip"),
            DocumentModel("Evidence Photos", "Images", "15 Jan 2025", "/mnt/data/le-z.zip"),
            DocumentModel("Agreement Copy", "PDF", "20 Jan 2025", "/mnt/data/le-z.zip")
        )

        binding.recyclerDocs.layoutManager = LinearLayoutManager(this)
        binding.recyclerDocs.adapter = DocumentAdapter(docs) { doc ->
            openDocument(doc.url)
        }
    }

    private fun openDocument(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(url), "*/*")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
