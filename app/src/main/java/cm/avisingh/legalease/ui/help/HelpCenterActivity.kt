package cm.avisingh.legalease.ui.help

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityHelpCenterBinding

class HelpCenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpCenterBinding
    private lateinit var faqAdapter: FAQAdapter
    private lateinit var guideAdapter: LegalGuideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerViews()
        setupSearch()
        loadData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerViews() {
        // Setup Guides RecyclerView
        binding.guidesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            guideAdapter = LegalGuideAdapter { guide ->
                // Handle guide click - navigate to guide detail
            }
            adapter = guideAdapter
        }

        // Setup FAQ RecyclerView
        binding.faqRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            faqAdapter = FAQAdapter()
            adapter = faqAdapter
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search submit
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter FAQs and guides based on search query
                return true
            }
        })
    }

    private fun loadData() {
        // Load guides
        val guides = listOf(
            LegalGuide(
                "Property Law",
                "How to Register Property",
                "A comprehensive guide on property registration process in India",
                R.drawable.img_property_registration
            ),
            LegalGuide(
                "Family Law",
                "Marriage Registration Guide",
                "Step-by-step guide for marriage registration in India",
                R.drawable.ic_notification // TODO: Create img_marriage_registration drawable
            ),
            // Add more guides
        )
        guideAdapter.submitList(guides)

        // Load FAQs
        val faqs = listOf(
            FAQ(
                "What documents are required for property registration?",
                "The essential documents required for property registration include:\n\n" +
                "1. Sale deed\n" +
                "2. Property title documents\n" +
                "3. Property tax receipts\n" +
                "4. Identity proof\n" +
                "5. Address proof\n" +
                "6. Recent photographs"
            ),
            FAQ(
                "How long does divorce proceedings take in India?",
                "The duration of divorce proceedings in India can vary:\n\n" +
                "• Mutual Consent Divorce: 6-18 months\n" +
                "• Contested Divorce: 2-5 years\n\n" +
                "The actual duration depends on various factors including court workload and complexity of the case."
            ),
            // Add more FAQs
        )
        faqAdapter.submitList(faqs)
    }
}

data class LegalGuide(
    val category: String,
    val title: String,
    val description: String,
    val imageResId: Int
)

data class FAQ(
    val question: String,
    val answer: String
)