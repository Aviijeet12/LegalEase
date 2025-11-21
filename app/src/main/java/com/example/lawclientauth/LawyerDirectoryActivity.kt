package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityLawyerDirectoryBinding

class LawyerDirectoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerDirectoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerDirectoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFilters()
        setupRecycler()
    }

    private fun setupFilters() {
        val specializations = listOf("All", "Civil", "Criminal", "Corporate", "Family", "Cyber", "Property")
        binding.dropdownSpecialization.setSimpleItems(specializations)
    }

    private fun setupRecycler() {

        val lawyerList = listOf(
            LawyerModel("Adv. A Sharma", "Civil Law", 4.8, 1200, 8),
            LawyerModel("Adv. R Mehra", "Criminal Law", 4.6, 900, 5),
            LawyerModel("Adv. S Kapoor", "Corporate Law", 4.9, 1500, 10),
            LawyerModel("Adv. J Rao", "Family Law", 4.5, 800, 6)
        )

        binding.recyclerLawyers.layoutManager = LinearLayoutManager(this)
        binding.recyclerLawyers.adapter = LawyerAdapter(lawyerList)
    }
}
