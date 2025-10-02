package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentKnowledgeBaseBinding

class KnowledgeBaseFragment : Fragment() {

    private lateinit var binding: FragmentKnowledgeBaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKnowledgeBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArticles()
    }

    private fun setupArticles() {
        val articles = listOf(
            Article(
                "Understanding Employment Contracts: A Complete Guide",
                "Learn everything you need to know about employment contracts, from basic terms to...",
                "12 min read",
                "2,847",
                "48",
                "Beginner",
                "Employment Law"
            ),
            Article(
                "How to Document Workplace Harassment",
                "Step-by-step guide on properly documenting workplace harassment incidents for legal...",
                "8 min read",
                "1,923",
                "49",
                "Intermediate",
                "Employment Law"
            )
        )

        val adapter = ArticlesAdapter(articles)
        binding.articlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.articlesRecyclerView.adapter = adapter
    }
}

data class Article(
    val title: String,
    val description: String,
    val readTime: String,
    val views: String,
    val likes: String,
    val level: String,
    val category: String
)