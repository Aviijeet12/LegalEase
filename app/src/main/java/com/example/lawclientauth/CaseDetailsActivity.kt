package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.CaseDetailsActivityBinding

class CaseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: CaseDetailsActivityBinding
    private lateinit var timelineAdapter: CaseTimelineAdapter
    private lateinit var documentAdapter: CaseDocumentAdapter

    private val mockDocUrl = "/mnt/data/le-z.zip" // Using uploaded zip file as sample doc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CaseDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCaseHeader()
        loadClientInfo()
        setupTimeline()
        setupDocuments()
        setupStatusDropdown()
        setupActions()
    }

    private fun loadCaseHeader() {
        binding.tvCaseTitle.text = "Property Dispute"
        binding.tvCaseId.text = "CASE10234"
        binding.tvCaseStatus.text = "Open"
    }

    private fun loadClientInfo() {
        binding.tvClientName.text = "Client: Rahul Verma"
        binding.tvClientPhone.text = "Phone: +91 98765 43210"
        binding.tvClientEmail.text = "Email: rahul@example.com"

        binding.btnClientCall.setOnClickListener {
            Toast.makeText(this, "Calling client (mock)…", Toast.LENGTH_SHORT).show()
        }

        binding.btnClientChat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }

    private fun setupTimeline() {
        val timeline = listOf(
            CaseTimelineModel("Case Created", "12 Jan 2025"),
            CaseTimelineModel("Documents Uploaded", "15 Jan 2025"),
            CaseTimelineModel("Court Hearing Scheduled", "20 Jan 2025"),
            CaseTimelineModel("Meeting with Client", "22 Jan 2025")
        )
        timelineAdapter = CaseTimelineAdapter(timeline)
        binding.recyclerTimeline.layoutManager = LinearLayoutManager(this)
        binding.recyclerTimeline.adapter = timelineAdapter
    }

    private fun setupDocuments() {
        val docs = listOf(
            CaseDocumentModel("Agreement Copy", mockDocUrl),
            CaseDocumentModel("Police Report", mockDocUrl),
            CaseDocumentModel("Court Notice", mockDocUrl),
        )
        documentAdapter = CaseDocumentAdapter(docs) { doc ->
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(doc.url)
                intent.setDataAndType(Uri.parse(doc.url), "*/*")
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to open document", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerDocuments.layoutManager = LinearLayoutManager(this)
        binding.recyclerDocuments.adapter = documentAdapter

        binding.btnUploadDocument.setOnClickListener {
            Toast.makeText(this, "Upload document (UI only)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupStatusDropdown() {
        val statuses = listOf("Open", "Hearing Soon", "Documents Pending", "Closed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, statuses)
        binding.dropdownStatus.adapter = adapter

        binding.dropdownStatus.setOnItemClickListener { _, _, position, _ ->
            val newStatus = statuses[position]
            binding.tvCaseStatus.text = newStatus
            Toast.makeText(this, "Case status updated (mock)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupActions() {
        binding.btnAddHearing.setOnClickListener {
            Toast.makeText(this, "Add hearing date (UI only)", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddNextStep.setOnClickListener {
            val notes = binding.etNotes.text.toString().trim()
            if (notes.isEmpty()) {
                Toast.makeText(this, "Add a note first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Next step added (mock)", Toast.LENGTH_SHORT).show()
        }
    }
}
