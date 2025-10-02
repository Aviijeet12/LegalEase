package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentAdminDashboardBinding
import com.nyayasetu.adapters.AdminActivityAdapter

class AdminDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAdminDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuickActions()
        setupRecentActivity()
    }

    private fun setupQuickActions() {
        binding.manageUsersCard.setOnClickListener {
            (requireActivity() as AdminMainActivity).showUsersFragment()
        }

        binding.manageLawyersCard.setOnClickListener {
            val fragment = LawyerManagementFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.systemAnalyticsCard.setOnClickListener {
            (requireActivity() as AdminMainActivity).showAnalyticsFragment()
        }

        binding.systemSettingsCard.setOnClickListener {
            (requireActivity() as AdminMainActivity).showSettingsFragment()
        }
    }

    private fun setupRecentActivity() {
        val activities = listOf(
            AdminActivity(
                "New user registered",
                "John Smith joined as a client",
                "5 minutes ago"
            ),
            AdminActivity(
                "Case completed",
                "Employment dispute case was resolved",
                "1 hour ago"
            ),
            AdminActivity(
                "Lawyer approved",
                "Sarah Johnson's profile was verified",
                "2 hours ago"
            ),
            AdminActivity(
                "System backup",
                "Daily system backup completed successfully",
                "3 hours ago"
            )
        )

        val adapter = AdminActivityAdapter(activities)
        binding.recentActivityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recentActivityRecyclerView.adapter = adapter
    }
}

data class AdminActivity(
    val title: String,
    val description: String,
    val time: String
)