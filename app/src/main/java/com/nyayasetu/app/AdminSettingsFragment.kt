package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nyayasetu.app.databinding.FragmentAdminSettingsBinding

class AdminSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAdminSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSettingsTabs()
        showGeneralTab()
    }

    private fun setupSettingsTabs() {
        binding.tabGeneral.setOnClickListener { showGeneralTab() }
        binding.tabSystem.setOnClickListener { showSystemTab() }
        binding.tabUsers.setOnClickListener { showUsersTab() }
        binding.tabOrganization.setOnClickListener { showOrganizationTab() }
    }

    private fun showGeneralTab() {
        updateSettingsTabSelection(0)
        binding.generalContent.visibility = View.VISIBLE
        binding.systemContent.visibility = View.GONE
        binding.usersContent.visibility = View.GONE
        binding.organizationContent.visibility = View.GONE
    }

    private fun showSystemTab() {
        updateSettingsTabSelection(1)
        binding.generalContent.visibility = View.GONE
        binding.systemContent.visibility = View.VISIBLE
        binding.usersContent.visibility = View.GONE
        binding.organizationContent.visibility = View.GONE
    }

    private fun showUsersTab() {
        updateSettingsTabSelection(2)
        binding.generalContent.visibility = View.GONE
        binding.systemContent.visibility = View.GONE
        binding.usersContent.visibility = View.VISIBLE
        binding.organizationContent.visibility = View.GONE
    }

    private fun showOrganizationTab() {
        updateSettingsTabSelection(3)
        binding.generalContent.visibility = View.GONE
        binding.systemContent.visibility = View.GONE
        binding.usersContent.visibility = View.GONE
        binding.organizationContent.visibility = View.VISIBLE
    }

    private fun updateSettingsTabSelection(selectedTab: Int) {
        val tabs = listOf(binding.tabGeneral, binding.tabSystem, binding.tabUsers, binding.tabOrganization)
        tabs.forEachIndexed { index, tab ->
            tab.isSelected = index == selectedTab
        }
    }
}