package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityAdminReportListBinding

class AdminReportListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminReportListBinding
    private lateinit var adapter: ReportAdapter
    private val reports = mutableListOf<ReportModel>()
    private var filteredReports = mutableListOf<ReportModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminReportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockReports()
        setupFilterDropdown()
        setupRecycler()
    }

    private fun loadMockReports() {
        reports.add(
            ReportModel(
                id = "r1",
                title = "Harassment by client",
                description = "The client used abusive language during the call.",
                filedBy = "Adv. Ananya Sharma",
                category = "Harassment",
                time = System.currentTimeMillis() - 3600_000,
                status = "Pending"
            )
        )
        reports.add(
            ReportModel(
                id = "r2",
                title = "Fake documents submitted",
                description = "The client provided invalid document proofs.",
                filedBy = "Adv. Rahul Khanna",
                category = "Fraud",
                time = System.currentTimeMillis() - 7200_000,
                status = "Resolved"
            )
        )
        reports.add(
            ReportModel(
                id = "r3",
                title = "Payment not received",
                description = "Payment was not released for the consultation.",
                filedBy = "Client: Rohan Mehta",
                category = "Payment Issue",
                time = System.currentTimeMillis() - 600_000,
                status = "Pending"
            )
        )

        filteredReports = reports.toMutableList()
    }

    private fun setupFilterDropdown() {
        val categories = listOf("All", "Harassment", "Fraud", "Payment Issue")
        val adapterDropdown = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapterDropdown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerFilter.adapter = adapterDropdown
        binding.spinnerFilter.setSelection(0)

        binding.spinnerFilter.setOnItemSelectedListener { _, _, pos, _ ->
            val selected = categories[pos]
            filterList(selected)
        }
    }

    private fun filterList(category: String) {
        filteredReports = if (category == "All") {
            reports.toMutableList()
        } else {
            reports.filter { it.category == category }.toMutableList()
        }
        adapter.updateList(filteredReports)
    }

    private fun setupRecycler() {
        adapter = ReportAdapter(filteredReports) { report ->
            val intent = Intent(this, AdminReportDetailsActivity::class.java)
            intent.putExtra("report_id", report.id)
            startActivity(intent)
        }
        binding.recyclerReports.layoutManager = LinearLayoutManager(this)
        binding.recyclerReports.adapter = adapter
    }
}
