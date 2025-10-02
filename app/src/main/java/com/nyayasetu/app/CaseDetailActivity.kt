package com.nyayasetu.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.ActivityCaseDetailBinding

class CaseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCaseDetails()
        setupDocuments()
        setupTimeline()
    }

    private fun setupCaseDetails() {
        binding.caseTitle.text = "Employment Contract Dispute"
        binding.clientName.text = "John Doe"
        binding.caseStatus.text = "In Progress"
        binding.casePriority.text = "High"
        binding.assignedLawyer.text = "Sarah Johnson"
        binding.caseDescription.text = "Contract dispute regarding employment terms and conditions. Client claims unfair termination clauses."
        binding.progressBar.progress = 65
    }

    private fun setupDocuments() {
        val documents = listOf(
            CaseDocument("Employment_Contract.pdf", "2.3 MB", "Today"),
            CaseDocument("Evidence_Photos.zip", "15.7 MB", "Today"),
            CaseDocument("Legal_Notes.docx", "1.2 MB", "Yesterday")
        )

        val adapter = CaseDocumentsAdapter(documents)
        binding.documentsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.documentsRecyclerView.adapter = adapter
    }

    private fun setupTimeline() {
        val timeline = listOf(
            CaseTimeline("Case Filed", "2024-01-15", "Case officially filed with all documentation"),
            CaseTimeline("Initial Review", "2024-01-18", "Initial case review completed"),
            CaseTimeline("Evidence Collection", "2024-01-20", "All evidence documents collected"),
            CaseTimeline("Legal Research", "2024-01-22", "Legal research phase completed")
        )

        val adapter = CaseTimelineAdapter(timeline)
        binding.timelineRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.timelineRecyclerView.adapter = adapter
    }
}

data class CaseDocument(val name: String, val size: String, val date: String)
data class CaseTimeline(val event: String, val date: String, val description: String)