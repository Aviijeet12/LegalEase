package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityLawyerCasesBinding

/**
 * Lawyer-facing case list screen.
 * - Search by title / case id
 * - Filter by status
 * - Tap to open CaseDetailsActivity
 * - View associated document (mock using uploaded path)
 */
class LawyerCasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerCasesBinding
    private val cases = mutableListOf<CaseModel>()
    private lateinit var adapter: LawyerCasesAdapter

    // Use uploaded file path as mock doc url (will be replaced by backend later)
    private val mockDocUrl = "/mnt/data/le-z.zip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerCasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockCases()
        setupFilter()
        setupRecycler()
        setupSearch()
    }

    private fun loadMockCases() {
        cases.clear()
        cases.add(CaseModel("Property Dispute", "CASE10234", "Open", "Adv. A Sharma", "12 Jan 2025"))
        cases.add(CaseModel("Criminal Appeal", "CASE10235", "Hearing Soon", "Adv. A Sharma", "10 Feb 2025"))
        cases.add(CaseModel("Family Settlement", "CASE10236", "Documents Pending", "Adv. A Sharma", "05 Feb 2025"))
        cases.add(CaseModel("Contract Breach", "CASE10237", "Closed", "Adv. A Sharma", "22 Dec 2024"))
    }

    private fun setupFilter() {
        val filters = listOf("All", "Open", "Hearing Soon", "Documents Pending", "Closed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filters)
        binding.dropdownStatus.setAdapter(adapter)

        binding.dropdownStatus.setOnItemClickListener { _, _, _, _ ->
            applyFilter(binding.dropdownStatus.text.toString())
        }
    }

    private fun applyFilter(filter: String) {
        if (filter == "All") {
            adapter.updateList(cases)
        } else {
            adapter.updateList(cases.filter { it.status == filter })
        }
    }

    private fun setupRecycler() {
        adapter = LawyerCasesAdapter(cases,
            onOpen = { caseModel ->
                // Open case details (reuses CaseDetailsActivity)
                val intent = Intent(this, CaseDetailsActivity::class.java)
                // You can pass details via extras later
                intent.putExtra("caseId", caseModel.caseId)
                startActivity(intent)
            },
            onViewDoc = { caseModel ->
                // Open document (mock)
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(mockDocUrl), "*/*")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Unable to open document", Toast.LENGTH_SHORT).show()
                }
            },
            onChangeStatus = { caseModel, newStatus ->
                // UI-only: update status locally
                caseModel.status = newStatus
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Status changed to $newStatus (mock)", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerLawyerCases.layoutManager = LinearLayoutManager(this)
        binding.recyclerLawyerCases.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.setOnClickListener { /* focus */ }
        binding.etSearch.addTextChangedListener { editable ->
            val q = editable?.toString()?.trim() ?: ""
            if (q.isEmpty()) {
                adapter.updateList(cases)
            } else {
                val filtered = cases.filter {
                    it.title.contains(q, ignoreCase = true) ||
                    it.caseId.contains(q, ignoreCase = true) ||
                    it.lawyerName.contains(q, ignoreCase = true)
                }
                adapter.updateList(filtered)
            }
        }
    }
}
