package com.nyayasetu.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        showHomeFragment()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showHomeFragment()
                    true
                }
                R.id.nav_chat -> {
                    showChatFragment()
                    true
                }
                R.id.nav_files -> {
                    showFilesFragment()
                    true
                }
                R.id.nav_alerts -> {
                    showAlertsFragment()
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

    fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }

    fun showChatFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChatFragment())
            .commit()
    }

    fun showFilesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FilesFragment())
            .commit()
    }

    fun showAlertsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AlertsFragment())
            .commit()
    }

    fun showProfileFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment())
            .commit()
    }
}