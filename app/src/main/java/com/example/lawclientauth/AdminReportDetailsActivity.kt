package com.example.lawclientauth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityAdminReportDetailsBinding

class AdminReportDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminReportDetailsBinding
    private var selectedReport: ReportModel? = null

    private val mockReports = listOf(
        ReportModel("r1", "Harassment by client", "Full details...", "Adv. Ananya", "Harassment", System.currentTimeMillis(), "Pending"),
        ReportModel("r2", "Fake documents", "Full details...", "Adv. Rahul", "Fraud", System.currentTimeMillis(), "Resolved"),
        ReportModel("r3", "Payment missing", "Full details...", "Client: Rohan", "Payment Issue", System.currentTimeMillis(), "Pending")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminReportDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("report_id")
        selectedReport = mockReports.find { it.id == id }

        displayDetails()
        setupResolveButton()
    }

    private fun displayDetails() {
        binding.tvTitle.text = selectedReport?.title
        binding.tvFiledBy.text = "Filed by: ${selectedReport?.filedBy}"
        binding.tvCategory.text = "Category: ${selectedReport?.category}"
        binding.tvDescription.text = selectedReport?.description

        if (selectedReport?.status == "Resolved") {
            binding.btnResolve.text = "Already Resolved"
            binding.btnResolve.isEnabled = false
        }
    }

    private fun setupResolveButton() {
        binding.btnResolve.setOnClickListener {
            Toast.makeText(this, "Report marked as resolved (UI only)", Toast.LENGTH_SHORT).show()
            binding.btnResolve.text = "Resolved"
            binding.btnResolve.isEnabled = false
        }
    }
}
