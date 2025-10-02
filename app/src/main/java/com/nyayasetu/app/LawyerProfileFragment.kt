package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nyayasetu.app.databinding.FragmentLawyerProfileBinding

class LawyerProfileFragment : Fragment() {

    private lateinit var binding: FragmentLawyerProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLawyerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileInfo()
        setupProfileTabs()
    }

    private fun setupProfileInfo() {
        binding.lawyerName.text = "Sarah Johnson, Esq."
        binding.lawyerSpecialization.text = "Employment Law, Corporate Law, Contract Law"
        binding.experience.text = "14 years experience"
        binding.rating.text = "4.8 rating"

        // Stats
        binding.totalCases.text = "247"
        binding.winRate.text = "88%"
        binding.ratingScore.text = "4.8"
        binding.yearlyRevenue.text = "$246K"
    }

    private fun setupProfileTabs() {
        binding.tabPersonal.setOnClickListener { showPersonalTab() }
        binding.tabProfessional.setOnClickListener { showProfessionalTab() }
        binding.tabSettings.setOnClickListener { showSettingsTab() }
        binding.tabPerformance.setOnClickListener { showPerformanceTab() }
    }

    private fun showPersonalTab() {
        updateProfileTabSelection(0)
        binding.personalContent.visibility = View.VISIBLE
        binding.professionalContent.visibility = View.GONE
        binding.settingsContent.visibility = View.GONE
        binding.performanceContent.visibility = View.GONE
    }

    private fun showProfessionalTab() {
        updateProfileTabSelection(1)
        binding.personalContent.visibility = View.GONE
        binding.professionalContent.visibility = View.VISIBLE
        binding.settingsContent.visibility = View.GONE
        binding.performanceContent.visibility = View.GONE
    }

    private fun showSettingsTab() {
        updateProfileTabSelection(2)
        binding.personalContent.visibility = View.GONE
        binding.professionalContent.visibility = View.GONE
        binding.settingsContent.visibility = View.VISIBLE
        binding.performanceContent.visibility = View.GONE
    }

    private fun showPerformanceTab() {
        updateProfileTabSelection(3)
        binding.personalContent.visibility = View.GONE
        binding.professionalContent.visibility = View.GONE
        binding.settingsContent.visibility = View.GONE
        binding.performanceContent.visibility = View.VISIBLE
    }

    private fun updateProfileTabSelection(selectedTab: Int) {
        val tabs = listOf(binding.tabPersonal, binding.tabProfessional, binding.tabSettings, binding.tabPerformance)
        tabs.forEachIndexed { index, tab ->
            tab.isSelected = index == selectedTab
        }
    }
}