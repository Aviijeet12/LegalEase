package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lawclientauth.databinding.ActivityKnowledgeBaseBinding

class KnowledgeBaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKnowledgeBaseBinding

    private val categories = mutableListOf<KnowledgeCategoryModel>()
    private val articles = mutableListOf<KnowledgeArticleModel>()

    private lateinit var categoryAdapter: KnowledgeCategoryAdapter
    private lateinit var articleAdapter: KnowledgeArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKnowledgeBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockData()
        setupCategoryRecycler()
        setupArticleRecycler()
        setupSearch()
    }

    private fun loadMockData() {
        categories.add(KnowledgeCategoryModel("Criminal Law", R.drawable.ic_case))
        categories.add(KnowledgeCategoryModel("Property Law", R.drawable.ic_document))
        categories.add(KnowledgeCategoryModel("Family Law", R.drawable.ic_profile_placeholder))
        categories.add(KnowledgeCategoryModel("Corporate Law", R.drawable.ic_calendar))
        categories.add(KnowledgeCategoryModel("Contract Law", R.drawable.ic_case))
        categories.add(KnowledgeCategoryModel("Civil Rights", R.drawable.ic_document))

        // articles (mock)
        articles.add(
            KnowledgeArticleModel(
                "How to File a Police Complaint",
                "Criminal Law",
                "Step-by-step guide to filing a complaint at your nearest police station.",
                "Filing a police complaint is the fundamental right ... (full article here)"
            )
        )
        articles.add(
            KnowledgeArticleModel(
                "Property Registration Guide",
                "Property Law",
                "Required documents and procedures for property registration.",
                "Property registration requires the following documents ... (full article)"
            )
        )
        articles.add(
            KnowledgeArticleModel(
                "Understanding Divorce Laws",
                "Family Law",
                "Basics of divorce filing, child custody, alimony.",
                "Divorce laws in India follow the Hindu Marriage Act ... (full article)"
            )
        )
        articles.add(
            KnowledgeArticleModel(
                "How to Draft a Contract",
                "Contract Law",
                "Elements of a valid business contract.",
                "The essentials of a valid contract include offer, acceptance ... (full content)"
            )
        )
    }

    private fun setupCategoryRecycler() {
        categoryAdapter = KnowledgeCategoryAdapter(categories) { cat ->
            filterArticles(cat.title)
        }
        binding.recyclerCategories.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerCategories.adapter = categoryAdapter
    }

    private fun setupArticleRecycler() {
        articleAdapter = KnowledgeArticleAdapter(articles) { article ->
            val intent = Intent(this, KnowledgeArticleActivity::class.java)
            intent.putExtra("title", article.title)
            intent.putExtra("category", article.category)
            intent.putExtra("summary", article.summary)
            intent.putExtra("content", article.fullContent)
            startActivity(intent)
        }
        binding.recyclerArticles.layoutManager = LinearLayoutManager(this)
        binding.recyclerArticles.adapter = articleAdapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s.toString().trim()
                if (q.isEmpty()) {
                    articleAdapter.updateList(articles)
                } else {
                    val filtered = articles.filter {
                        it.title.contains(q, ignoreCase = true) ||
                                it.category.contains(q, ignoreCase = true)
                    }
                    articleAdapter.updateList(filtered)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(s: CharSequence?, i: Int, i2: Int, i3: Int) {}
        })
    }

    private fun filterArticles(category: String) {
        val filtered = articles.filter { it.category == category }
        articleAdapter.updateList(filtered)
    }
}
