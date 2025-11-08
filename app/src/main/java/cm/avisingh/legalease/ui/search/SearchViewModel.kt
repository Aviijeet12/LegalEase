package cm.avisingh.legalease.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    // Advanced search filters
    private val selectedCategories = mutableSetOf<DocumentCategory>()
    private val selectedTypes = mutableSetOf<DocumentType>()
    private var dateRange: Pair<Date, Date>? = null
    private var sizeRange: Pair<Int, Int> = Pair(0, 100)
    private var currentSortOption: SortOption = SortOption.RELEVANCE

    init {
        loadSearchHistory()
        setupSearchQueryListener()
    }

    private fun setupSearchQueryListener() {
        _searchQuery
            .debounce(300)
            .onEach { query ->
                if (query.length >= 2) {
                    performSearch(query)
                } else {
                    _searchResults.value = emptyList()
                }
            }
            .launchIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = searchRepository.search(
                    query = query,
                    categories = selectedCategories,
                    types = selectedTypes,
                    dateRange = dateRange,
                    sizeRange = sizeRange,
                    sortOption = currentSortOption
                )
                _searchResults.value = results
                
                // Add to search history if not already present
                if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
                    val updatedHistory = (_searchHistory.value + query).take(10)
                    _searchHistory.value = updatedHistory
                    saveSearchHistory(updatedHistory)
                }
            } catch (e: Exception) {
                // Handle error
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCategory(category: DocumentCategory) {
        selectedCategories.add(category)
        refreshSearch()
    }

    fun removeCategory(category: DocumentCategory) {
        selectedCategories.remove(category)
        refreshSearch()
    }

    fun addDocumentType(type: DocumentType) {
        selectedTypes.add(type)
        refreshSearch()
    }

    fun removeDocumentType(type: DocumentType) {
        selectedTypes.remove(type)
        refreshSearch()
    }

    fun setDateRange(start: Date, end: Date) {
        dateRange = Pair(start, end)
        refreshSearch()
    }

    fun setSizeRange(min: Int, max: Int) {
        sizeRange = Pair(min, max)
        refreshSearch()
    }

    fun setSortOption(option: SortOption) {
        currentSortOption = option
        refreshSearch()
    }

    fun resetFilters() {
        selectedCategories.clear()
        selectedTypes.clear()
        dateRange = null
        sizeRange = Pair(0, 100)
        currentSortOption = SortOption.RELEVANCE
        refreshSearch()
    }

    private fun refreshSearch() {
        _searchQuery.value.let { query ->
            if (query.length >= 2) {
                performSearch(query)
            }
        }
    }

    fun applyAdvancedSearch() {
        refreshSearch()
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            _searchHistory.value = emptyList()
            searchRepository.clearSearchHistory()
        }
    }

    private fun loadSearchHistory() {
        viewModelScope.launch {
            _searchHistory.value = searchRepository.getSearchHistory()
        }
    }

    private fun saveSearchHistory(history: List<String>) {
        viewModelScope.launch {
            searchRepository.saveSearchHistory(history)
        }
    }
}