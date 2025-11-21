package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityClientDashboardBinding

class ClientDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClicks()
    }

    private fun setupClicks() {

        binding.cardFindLawyer.setOnClickListener { }
        binding.cardMyCases.setOnClickListener { }
        binding.cardAppointments.setOnClickListener { }
        binding.cardAiMatch.setOnClickListener { }
        binding.cardPayments.setOnClickListener { }
        binding.cardInvoices.setOnClickListener { }
        binding.cardHelpCenter.setOnClickListener { }
        binding.cardSavedLawyers.setOnClickListener { }
        binding.cardRecentActivity.setOnClickListener { }

        // Bottom Nav
        binding.navHome.setOnClickListener { }
        binding.navCases.setOnClickListener { }
        binding.navProfile.setOnClickListener { }
    }
}
