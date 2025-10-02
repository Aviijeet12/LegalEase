package com.nyayasetu.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
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
                R.id.nav_users -> {
                    showUsersFragment()
                    true
                }
                R.id.nav_files -> {
                    showFilesFragment()
                    true
                }
                R.id.nav_analytics -> {
                    showAnalyticsFragment()
                    true
                }
                R.id.nav_settings -> {
                    showSettingsFragment()
                    true
                }
                else -> false
            }
        }
    }

    fun showDashboardFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AdminDashboardFragment())
            .commit()
    }

    fun showUsersFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AdminUsersFragment())
            .commit()
    }

    fun showFilesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AdminFilesFragment())
            .commit()
    }

    fun showAnalyticsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AdminAnalyticsFragment())
            .commit()
    }

    fun showSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AdminSettingsFragment())
            .commit()
    }
}