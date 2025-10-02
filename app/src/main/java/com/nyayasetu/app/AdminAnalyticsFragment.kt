package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nyayasetu.app.databinding.FragmentAdminAnalyticsBinding

class AdminAnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentAdminAnalyticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnalyticsTabs()
        showSystemTab()
    }

    private fun setupAnalyticsTabs() {
        binding.tabSystem.setOnClickListener { showSystemTab() }
        binding.tabUsers.setOnClickListener { showUsersTab() }
        binding.tabCases.setOnClickListener { showCasesTab() }
        binding.tabRevenue.setOnClickListener { showRevenueTab() }
    }

    private fun showSystemTab() {
        updateAnalyticsTabSelection(0)
        binding.systemContent.visibility = View.VISIBLE
        binding.usersContent.visibility = View.GONE
        binding.casesContent.visibility = View.GONE
        binding.revenueContent.visibility = View.GONE
    }

    private fun showUsersTab() {
        updateAnalyticsTabSelection(1)
        binding.systemContent.visibility = View.GONE
        binding.usersContent.visibility = View.VISIBLE
        binding.casesContent.visibility = View.GONE
        binding.revenueContent.visibility = View.GONE
    }

    private fun showCasesTab() {
        updateAnalyticsTabSelection(2)
        binding.systemContent.visibility = View.GONE
        binding.usersContent.visibility = View.GONE
        binding.casesContent.visibility = View.VISIBLE
        binding.revenueContent.visibility = View.GONE
    }

    private fun showRevenueTab() {
        updateAnalyticsTabSelection(3)
        binding.systemContent.visibility = View.GONE
        binding.usersContent.visibility = View.GONE
        binding.casesContent.visibility = View.GONE
        binding.revenueContent.visibility = View.VISIBLE
    }

    private fun updateAnalyticsTabSelection(selectedTab: Int) {
        val tabs = listOf(binding.tabSystem, binding.tabUsers, binding.tabCases, binding.tabRevenue)
        tabs.forEachIndexed { index, tab ->
            tab.isSelected = index == selectedTab
        }
    }
}