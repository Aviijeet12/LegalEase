package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentLawyerCasesBinding

class LawyerCasesFragment : Fragment() {

    private lateinit var binding: FragmentLawyerCasesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLawyerCasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCases()
        setupFilterTabs()
    }

    private fun setupFilterTabs() {
        binding.tabAll.setOnClickListener { updateTabSelection(0) }
        binding.tabActive.setOnClickListener { updateTabSelection(1) }
        binding.tabPending.setOnClickListener { updateTabSelection(2) }
        binding.tabCompleted.setOnClickListener { updateTabSelection(3) }
        binding.tabOverdue.setOnClickListener { updateTabSelection(4) }
    }

    private fun updateTabSelection(selectedTab: Int) {
        val tabs = listOf(binding.tabAll, binding.tabActive, binding.tabPending, binding.tabCompleted, binding.tabOverdue)
        tabs.forEachIndexed { index, tab ->
            tab.isSelected = index == selectedTab
            tab.setBackgroundColor(
                if (index == selectedTab) getColor(android.R.color.holo_blue_light)
                else getColor(android.R.color.transparent)
            )
        }
    }

    private fun setupCases() {
        val cases = listOf(
            LawyerCase(
                "Employment Contract Dispute",
                "John Doe",
                "High",
                "$2,500.00",
                "12.5h logged",
                "In Progress",
                "Feb 15",
                "65%",
                "2 hours ago"
            ),
            LawyerCase(
                "Property Purchase Agreement",
                "Jane Smith",
                "Medium",
                "$1,800.00",
                "8h logged",
                "Pending Review",
                "Feb 20",
                "30%",
                "1 day ago"
            ),
            LawyerCase(
                "Business Partnership Agreement",
                "Mike Johnson",
                "Low",
                "$3,200.00",
                "15h logged",
                "In Progress",
                "Feb 25",
                "45%",
                "2 days ago"
            )
        )

        val adapter = LawyerCasesAdapter(cases)
        binding.casesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.casesRecyclerView.adapter = adapter
    }
}

data class LawyerCase(
    val title: String,
    val client: String,
    val priority: String,
    val amount: String,
    val hours: String,
    val status: String,
    val dueDate: String,
    val progress: String,
    val lastUpdate: String
)