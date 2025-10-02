package com.nyayasetu.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.ActivitySearchLawyersBinding

class SearchLawyersActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchLawyersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchLawyersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSearch()
        setupLawyersList()
        setupFilterChips()
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterLawyers(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterLawyers(newText)
                return true
            }
        })
    }

    private fun setupFilterChips() {
        val practiceAreas = listOf("Employment Law", "Family Law", "Criminal Law", "Corporate Law", "Real Estate", "Intellectual Property")

        practiceAreas.forEach { area ->
            val chip = com.google.android.material.chip.Chip(this).apply {
                text = area
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        filterByPracticeArea(area)
                    }
                }
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun setupLawyersList() {
        val lawyers = listOf(
            Lawyer(
                "Sarah Johnson",
                "Employment Law, Corporate Law",
                "4.8",
                "14 years experience",
                "247 cases",
                "$250/hr"
            ),
            Lawyer(
                "Michael Chen",
                "Contract Law, Real Estate",
                "4.7",
                "12 years experience",
                "189 cases",
                "$200/hr"
            ),
            Lawyer(
                "Robert Wilson",
                "Family Law, Civil Law",
                "4.6",
                "10 years experience",
                "156 cases",
                "$180/hr"
            )
        )

        val adapter = LawyersAdapter(lawyers) { lawyer ->
            // Navigate to lawyer profile
        }

        binding.lawyersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.lawyersRecyclerView.adapter = adapter
    }

    private fun filterLawyers(query: String) {
        // Implement search filtering
    }

    private fun filterByPracticeArea(area: String) {
        // Implement practice area filtering
    }
}

data class Lawyer(
    val name: String,
    val specialization: String,
    val rating: String,
    val experience: String,
    val cases: String,
    val rate: String
)