package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nyayasetu.app.databinding.FragmentLawyerAnalyticsBinding

class LawyerAnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentLawyerAnalyticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLawyerAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnalyticsTabs()
        showOverviewTab()
    }

    private fun setupAnalyticsTabs() {
        binding.tabOverview.setOnClickListener { showOverviewTab() }
        binding.tabPerformance.setOnClickListener { showPerformanceTab() }
        binding.tabClientStats.setOnClickListener { showClientStatsTab() }
        binding.tabRevenue.setOnClickListener { showRevenueTab() }
    }

    private fun showOverviewTab() {
        updateAnalyticsTabSelection(0)
        binding.overviewContent.visibility = View.VISIBLE
        binding.performanceContent.visibility = View.GONE
        binding.clientStatsContent.visibility = View.GONE
        binding.revenueContent.visibility = View.GONE
    }

    private fun showPerformanceTab() {
        updateAnalyticsTabSelection(1)
        binding.overviewContent.visibility = View.GONE
        binding.performanceContent.visibility = View.VISIBLE
        binding.clientStatsContent.visibility = View.GONE
        binding.revenueContent.visibility = View.GONE
    }

    private fun showClientStatsTab() {
        updateAnalyticsTabSelection(2)
        binding.overviewContent.visibility = View.GONE
        binding.performanceContent.visibility = View.GONE
        binding.clientStatsContent.visibility = View.VISIBLE
        binding.revenueContent.visibility = View.GONE
    }

    private fun showRevenueTab() {
        updateAnalyticsTabSelection(3)
        binding.overviewContent.visibility = View.GONE
        binding.performanceContent.visibility = View.GONE
        binding.clientStatsContent.visibility = View.GONE
        binding.revenueContent.visibility = View.VISIBLE
    }

    private fun updateAnalyticsTabSelection(selectedTab: Int) {
        val tabs = listOf(binding.tabOverview, binding.tabPerformance, binding.tabClientStats, binding.tabRevenue)
        tabs.forEachIndexed { index, tab ->
            tab.isSelected = index == selectedTab
        }
    }
}