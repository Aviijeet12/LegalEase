package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentFilesBinding

class FilesFragment : Fragment() {

    private lateinit var binding: FragmentFilesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFiles()
        setupCategories()
    }

    private fun setupCategories() {
        val categories = listOf("Contracts", "Evidence", "Notes", "Legal Briefs")
        // Setup category chips
    }

    private fun setupFiles() {
        val files = listOf(
            Document(
                "Employment_Contract_2024.pdf",
                "2.3 MB",
                "Today",
                "Employment Contract Dispute"
            ),
            Document(
                "Workplace_Incident_Photos.zip",
                "15.7 MB",
                "Today",
                "Employment Contract Dispute"
            ),
            Document(
                "Legal_Consultation_Notes.docx",
                "1.2 MB",
                "Yesterday",
                "Employment Contract Dispute"
            ),
            Document(
                "Property_Deed.pdf",
                "3.8 MB",
                "2 days ago",
                "Property Purchase"
            )
        )

        val adapter = DocumentsAdapter(files)
        binding.documentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.documentsRecyclerView.adapter = adapter
    }
}

data class Document(
    val name: String,
    val size: String,
    val date: String,
    val case: String
)