package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityCaseListBinding

class CaseListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFilters()
        setupRecycler()
    }

    private fun setupFilters() {
        val caseTypes = listOf("All", "Civil", "Criminal", "Corporate", "Family", "Cyber", "Property")
        val caseStatus = listOf("All", "Open", "Closed", "Pending")

        binding.dropdownCaseType.setSimpleItems(caseTypes)
        binding.dropdownCaseStatus.setSimpleItems(caseStatus)
    }

    private fun setupRecycler() {

        val caseList = listOf(
            CaseModel("Property Dispute", "CASE10234", "Open", "Adv. A Sharma", "12 Jan 2025"),
            CaseModel("Cyber Fraud Complaint", "CASE23901", "Pending", "Adv. R Mehra", "08 Jan 2025"),
            CaseModel("Corporate Contract Issue", "CASE99882", "Closed", "Adv. S Kapoor", "28 Dec 2024")
        )

        binding.recyclerCases.layoutManager = LinearLayoutManager(this)
        binding.recyclerCases.adapter = CaseAdapter(caseList)
    }
}
