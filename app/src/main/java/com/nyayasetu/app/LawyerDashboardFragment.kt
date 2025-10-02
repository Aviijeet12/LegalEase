package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentLawyerDashboardBinding

class LawyerDashboardFragment : Fragment() {

    private lateinit var binding: FragmentLawyerDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLawyerDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuickActions()
        setupRecentActivity()
    }

    private fun setupQuickActions() {
        binding.manageCasesCard.setOnClickListener {
            (requireActivity() as LawyerMainActivity).showCasesFragment()
        }

        binding.viewClientsCard.setOnClickListener {
            val fragment = ClientManagementFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.analyticsCard.setOnClickListener {
            (requireActivity() as LawyerMainActivity).showAnalyticsFragment()
        }

        binding.scheduleCard.setOnClickListener {
            // Navigate to schedule screen
        }
    }

    private fun setupRecentActivity() {
        val activities = listOf(
            RecentActivity(
                "Case progress updated",
                "Employment Contract Dispute - 65% complete",
                "2 hours ago"
            ),
            RecentActivity(
                "New client message",
                "John Doe sent you a message",
                "4 hours ago"
            ),
            RecentActivity(
                "Document uploaded",
                "Contract review completed for Jane Smith",
                "1 day ago"
            )
        )

        val adapter = RecentActivityAdapter(activities)
        binding.recentActivityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recentActivityRecyclerView.adapter = adapter
    }
}

data class RecentActivity(
    val title: String,
    val description: String,
    val time: String
)