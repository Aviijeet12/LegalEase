package com.example.lawclientauth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityAiLawyerMatchBinding

class AiLawyerMatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiLawyerMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiLawyerMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFilters()
        setupActions()
    }

    private fun setupFilters() {
        // Lawyer type dropdown (static list for now)
        val lawyerTypes = listOf("Civil", "Criminal", "Corporate", "Family", "Cyber", "Property")
        binding.dropdownLawyerType.setSimpleItems(lawyerTypes)
    }

    private fun setupActions() {

        binding.btnFindMatch.setOnClickListener {

            // FRONTEND ONLY
            Toast.makeText(this, "Finding your best match...", Toast.LENGTH_SHORT).show()

            binding.cardResult.visibility = View.VISIBLE
        }
    }
}
