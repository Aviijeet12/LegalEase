package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nyayasetu.app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileInfo()
        setupMenuItems()
    }

    private fun setupProfileInfo() {
        // Set user data
        binding.userName.text = "John Doe"
        binding.userEmail.text = "john.doe@email.com"
        binding.memberSince.text = "Member since January 15, 2023"
    }

    private fun setupMenuItems() {
        binding.personalSettings.setOnClickListener {
            // Navigate to personal settings
        }

        binding.history.setOnClickListener {
            // Navigate to history
        }

        binding.billing.setOnClickListener {
            // Navigate to billing
        }
    }
}