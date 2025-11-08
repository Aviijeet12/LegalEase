package cm.avisingh.legalease.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivitySearchBinding
import cm.avisingh.legalease.ui.guide.GuideDetailActivity
import cm.avisingh.legalease.ui.pdf.PdfViewerActivity
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: SearchResultAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSearchInput()
        setupRecyclerView()
        setupFilters()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupSearchInput() {
        binding.searchEditText.apply {
            requestFocus()
            doAfterTextChanged { text ->
                viewModel.setSearchQuery(text?.toString() ?: "")
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.setSearchQuery(text.toString())
                    true
                } else false
            }
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchResultAdapter { result ->
            when (result.type) {
                SearchResultType.GUIDE -> {
                    // Navigate to guide detail with transition
                    result.imageView?.let { imageView ->
                        GuideDetailActivity.start(
                            this,
                            result.id,
                            imageView
                        )
                    }
                }
                SearchResultType.DOCUMENT -> {
                    // Open document in PDF viewer
                    startActivity(
                        PdfViewerActivity.createIntent(
                            this,
                            result.id,
                            result.title
                        )
                    )
                }
                SearchResultType.FAQ -> {
                    // Show FAQ detail in bottom sheet
                    FAQDetailBottomSheet.show(
                        supportFragmentManager,
                        result.id
                    )
                }
                SearchResultType.LAWYER -> {
                    // Navigate to lawyer profile
                    LawyerProfileActivity.start(this, result.id)
                }
            }
        }

        binding.resultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
            itemAnimator?.apply {
                addDuration = 200
                moveDuration = 200
                changeDuration = 200
                removeDuration = 200
            }
        }
    }

    private fun setupFilters() {
        binding.filterChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val filters = mutableSetOf<SearchResultType>()
            if (binding.guidesChip.isChecked) filters.add(SearchResultType.GUIDE)
            if (binding.faqsChip.isChecked) filters.add(SearchResultType.FAQ)
            if (binding.documentsChip.isChecked) filters.add(SearchResultType.DOCUMENT)
            if (binding.lawyersChip.isChecked) filters.add(SearchResultType.LAWYER)
            
            viewModel.setSearchQuery(binding.searchEditText.text.toString())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.searchResults.collect { results ->
                updateUI(results, viewModel.isLoading.value)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                updateUI(viewModel.searchResults.value, loading)
            }
        }

        lifecycleScope.launch {
            viewModel.searchHistory.collect { history ->
                // Update search suggestions
                val adapter = ArrayAdapter(
                    this@SearchActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    history
                )
                // TODO: searchEditText needs to be AutoCompleteTextView to use setAdapter
                // binding.searchEditText.setAdapter(adapter)
            }
        }
    }

    private fun updateUI(results: List<SearchResult>, loading: Boolean) {
        binding.apply {
            loadingIndicator.isVisible = loading
            resultsRecyclerView.isVisible = results.isNotEmpty()
            emptyStateLayout.isVisible = !loading && results.isEmpty()

            if (results.isNotEmpty()) {
                searchAdapter.submitList(results)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_clear_history -> {
                viewModel.clearSearchHistory()
                true
            }
            R.id.action_advanced_search -> {
                showAdvancedSearchDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAdvancedSearchDialog() {
        AdvancedSearchDialog().show(supportFragmentManager, "ADVANCED_SEARCH")
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SearchActivity::class.java))
        }
    }
}