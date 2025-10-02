package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentAdminFilesBinding

class AdminFilesFragment : Fragment() {

    private lateinit var binding: FragmentAdminFilesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryTabs()
        setupDocuments()
    }

    private fun setupCategoryTabs() {
        binding.tabAll.setOnClickListener { updateFileTabSelection(0) }
        binding.tabContracts.setOnClickListener { updateFileTabSelection(1) }
        binding.tabEvidence.setOnClickListener { updateFileTabSelection(2) }
        binding.tabNotes.setOnClickListener { updateFileTabSelection(3) }
        binding.tabLegal.setOnClickListener { updateFileTabSelection(4) }
    }

    private fun updateFileTabSelection(selectedTab: Int) {
        val tabs = listOf(binding.tabAll, binding.tabContracts, binding.tabEvidence, binding.tabNotes, binding.tabLegal)
        tabs.forEachIndexed { index, tab ->
            tab.isSelected = index == selectedTab
            tab.setBackgroundColor(
                if (index == selectedTab) getColor(android.R.color.holo_blue_light)
                else getColor(android.R.color.transparent)
            )
        }
    }

    private fun setupDocuments() {
        val documents = listOf(
            AdminDocument(
                "Employment_Contract_2024.pdf",
                "2.3 MB",
                "Today",
                "Employment Contract Dispute",
                "Contracts"
            ),
            AdminDocument(
                "Workplace_Incident_Photos.zip",
                "15.7 MB",
                "Today",
                "Employment Contract Dispute",
                "Evidence"
            ),
            AdminDocument(
                "Legal_Consultation_Notes.docx",
                "1.2 MB",
                "Yesterday",
                "Employment Contract Dispute",
                "Notes"
            ),
            AdminDocument(
                "Property_Deed.pdf",
                "3.8 MB",
                "2 days ago",
                "Property Purchase Agreement",
                "Legal Documents"
            )
        )

        val adapter = AdminDocumentsAdapter(documents)
        binding.documentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.documentsRecyclerView.adapter = adapter
    }
}

data class AdminDocument(
    val name: String,
    val size: String,
    val date: String,
    val case: String,
    val category: String
)