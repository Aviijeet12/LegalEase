package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityCreateCaseBinding

class CreateCaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCaseBinding
    private val uploadedDocuments = mutableListOf<DocumentModel>()
    private lateinit var adapter: DocumentAdapter

    private val mockUploadedUrl = "/mnt/data/le-z.zip" // uploaded file path (tool will convert it later)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCaseTypeDropdown()
        setupRecycler()
        setupClicks()
    }

    private fun setupCaseTypeDropdown() {
        val types = listOf(
            "Civil",
            "Criminal",
            "Corporate",
            "Family",
            "Cyber Crime",
            "Property",
            "Contract",
            "Consumer Dispute"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, types)
        binding.dropdownCaseType.setAdapter(adapter)
    }

    private fun setupRecycler() {
        adapter = DocumentAdapter(uploadedDocuments) { doc ->
            openDocument(doc.url)
        }
        binding.recyclerUploadedDocs.layoutManager = LinearLayoutManager(this)
        binding.recyclerUploadedDocs.adapter = adapter
    }

    private fun setupClicks() {
        binding.btnChooseFile.setOnClickListener {
            // For now: we mock the upload with the uploaded file path
            uploadedDocuments.add(
                DocumentModel(
                    title = "Uploaded File",
                    type = "PDF",
                    date = "Today",
                    url = mockUploadedUrl
                )
            )
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "File added (mock).", Toast.LENGTH_SHORT).show()
        }

        binding.btnSubmitCase.setOnClickListener {
            submitCase()
        }
    }

    private fun openDocument(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(url), "*/*")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitCase() {
        val title = binding.etCaseTitle.text.toString().trim()
        val type = binding.dropdownCaseType.text.toString().trim()
        val desc = binding.etDescription.text.toString().trim()

        if (title.isEmpty() || type.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Case submitted (mock)", Toast.LENGTH_LONG).show()

        // Later: send data to backend
        finish()
    }
}
