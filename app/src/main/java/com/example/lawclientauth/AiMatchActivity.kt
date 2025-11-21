package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityAiMatchBinding

class AiMatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFilters()
        setupRecycler()
    }

    private fun setupFilters() {
        val types = listOf("All", "Civil", "Criminal", "Corporate", "Family", "Property", "Cyber")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, types)
        binding.dropdownType.setAdapter(typeAdapter)

        val ratings = listOf("All", "4.0+", "4.5+", "4.8+", "5.0")
        val ratingAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ratings)
        binding.dropdownRating.setAdapter(ratingAdapter)

        val prices = listOf("All", "Below ₹500", "₹500–₹1000", "₹1000–₹2000", "Above ₹2000")
        val priceAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, prices)
        binding.dropdownPrice.setAdapter(priceAdapter)
    }

    private fun setupRecycler() {
        val lawyerList = listOf(
            AiLawyerModel("Adv. A Sharma", "Civil Law", 4.8, 1200, 8, 96),
            AiLawyerModel("Adv. R Mehra", "Criminal Law", 4.6, 900, 5, 92),
            AiLawyerModel("Adv. S Kapoor", "Corporate Law", 4.9, 1500, 10, 98),
            AiLawyerModel("Adv. J Rao", "Family Law", 4.5, 800, 6, 89)
        )

        binding.recyclerAiLawyers.layoutManager = LinearLayoutManager(this)
        binding.recyclerAiLawyers.adapter = AiLawyerAdapter(lawyerList) { lawyer ->
            val intent = Intent(this, LawyerProfileActivity::class.java)
            intent.putExtra("lawyerName", lawyer.name)
            startActivity(intent)
        }
    }
}
