package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuickActions()
        setupRecentCases()
    }

    private fun setupQuickActions() {
        binding.submitCaseCard.setOnClickListener {
            // Navigate to submit case screen
        }

        binding.chatSupportCard.setOnClickListener {
            // Navigate to chat support
        }

        binding.knowledgeBaseCard.setOnClickListener {
            // Navigate to knowledge base
            val fragment = KnowledgeBaseFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.notificationsCard.setOnClickListener {
            // Navigate to notifications
            (requireActivity() as MainActivity).showAlertsFragment()
        }
    }

    private fun setupRecentCases() {
        val cases = listOf(
            RecentCase(
                "Employment Contract Dispute",
                "Sarah Johnson",
                "In Progress",
                "2 hours ago"
            ),
            RecentCase(
                "Property Purchase Agreement",
                "Michael Chen",
                "Pending Review",
                "1 day ago"
            )
        )

        val adapter = RecentCasesAdapter(cases)
        binding.recentCasesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recentCasesRecyclerView.adapter = adapter

        binding.viewAllCases.setOnClickListener {
            // Navigate to all cases screen
        }
    }
}

data class RecentCase(
    val title: String,
    val lawyer: String,
    val status: String,
    val time: String
)