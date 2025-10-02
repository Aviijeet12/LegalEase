package com.nyayasetu.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.databinding.ActivityLawyerMainBinding

class LawyerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        showDashboardFragment()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    showDashboardFragment()
                    true
                }
                R.id.nav_cases -> {
                    showCasesFragment()
                    true
                }
                R.id.nav_chat -> {
                    showChatFragment()
                    true
                }
                R.id.nav_analytics -> {
                    showAnalyticsFragment()
                    true
                }
                R.id.nav_profile -> {
                    showProfileFragment()
                    true
                }
                else -> false
            }
        }
    }

    fun showDashboardFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LawyerDashboardFragment())
            .commit()
    }

    fun showCasesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LawyerCasesFragment())
            .commit()
    }

    fun showChatFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LawyerChatFragment())
            .commit()
    }

    fun showAnalyticsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LawyerAnalyticsFragment())
            .commit()
    }

    fun showProfileFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LawyerProfileFragment())
            .commit()
    }
}