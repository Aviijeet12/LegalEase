package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivitySavedLawyersBinding

class SavedLawyersActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedLawyersBinding
    private lateinit var adapter: SavedLawyerAdapter
    private val savedList = mutableListOf<AiLawyerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedLawyersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockData()
        setupRecycler()
    }

    private fun loadMockData() {
        savedList.add(
            AiLawyerModel("Adv. A Sharma", "Civil Law", 4.8, 1200, 8, 96)
        )
        savedList.add(
            AiLawyerModel("Adv. S Kapoor", "Corporate Law", 4.9, 1500, 10, 98)
        )
    }

    private fun setupRecycler() {
        adapter = SavedLawyerAdapter(savedList,
            onViewProfile = { lawyer ->
                val intent = Intent(this, LawyerProfileActivity::class.java)
                intent.putExtra("lawyerName", lawyer.name)
                startActivity(intent)
            },
            onRemove = { lawyer ->
                savedList.remove(lawyer)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
            })

        binding.recyclerSaved.layoutManager = LinearLayoutManager(this)
        binding.recyclerSaved.adapter = adapter
    }
}
