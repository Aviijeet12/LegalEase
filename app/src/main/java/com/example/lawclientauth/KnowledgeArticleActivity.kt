package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityKnowledgeArticleBinding

class KnowledgeArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKnowledgeArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKnowledgeArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: ""
        val category = intent.getStringExtra("category") ?: ""
        val summary = intent.getStringExtra("summary") ?: ""
        val content = intent.getStringExtra("content") ?: ""

        binding.tvTitle.text = title
        binding.tvCategory.text = category
        binding.tvSummary.text = summary
        binding.tvContent.text = content

        binding.btnBookmark.setOnClickListener {
            binding.btnBookmark.text = "Bookmarked ✔"
        }
    }
}
