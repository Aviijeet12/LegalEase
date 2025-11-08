package cm.avisingh.legalease.ui.search

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cm.avisingh.legalease.data.model.Document
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("search_prefs")
private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")

@Singleton
class SearchRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore
) {
    suspend fun search(
        query: String,
        categories: Set<DocumentCategory> = emptySet(),
        types: Set<DocumentType> = emptySet(),
        dateRange: Pair<Date, Date>? = null,
        sizeRange: Pair<Int, Int>? = null,
        sortOption: SortOption = SortOption.RELEVANCE
    ): List<SearchResult> {
        val results = mutableListOf<SearchResult>()

        // Build base query
        var documentsQuery = firestore.collection("documents")
            .whereArrayContains("searchKeywords", query.toLowerCase())

        // Apply filters
        if (categories.isNotEmpty()) {
            documentsQuery = documentsQuery.whereIn("category", categories.map { it.name })
        }

        if (types.isNotEmpty()) {
            documentsQuery = documentsQuery.whereIn("type", types.map { it.name })
        }

        dateRange?.let { (start, end) ->
            documentsQuery = documentsQuery
                .whereGreaterThanOrEqualTo("createdAt", start)
                .whereLessThanOrEqualTo("createdAt", end)
        }

        // Apply sorting
        documentsQuery = when (sortOption) {
            SortOption.DATE_DESC -> documentsQuery.orderBy("createdAt", Query.Direction.DESCENDING)
            SortOption.DATE_ASC -> documentsQuery.orderBy("createdAt", Query.Direction.ASCENDING)
            SortOption.NAME_ASC -> documentsQuery.orderBy("title", Query.Direction.ASCENDING)
            SortOption.NAME_DESC -> documentsQuery.orderBy("title", Query.Direction.DESCENDING)
            SortOption.SIZE_DESC -> documentsQuery.orderBy("size", Query.Direction.DESCENDING)
            SortOption.SIZE_ASC -> documentsQuery.orderBy("size", Query.Direction.ASCENDING)
            SortOption.RELEVANCE -> documentsQuery.orderBy("relevanceScore", Query.Direction.DESCENDING)
        }

        // Execute query
        val documents = documentsQuery.get().await().documents.mapNotNull { doc ->
            doc.toObject(Document::class.java)?.let { document ->
                // Post-filter for size range (Firestore doesn't support range queries on multiple fields)
                if (sizeRange != null) {
                    val (minSize, maxSize) = sizeRange
                    if (document.size < minSize || document.size > maxSize) {
                        return@let null
                    }
                }

                SearchResult(
                    id = document.id,
                    type = SearchResultType.DOCUMENT,
                    title = document.title,
                    description = document.description,
                    category = document.category,
                    date = document.createdAt,
                    size = document.size,
                    documentType = document.type,
                    iconResId = getIconForDocumentType(document.type),
                    matchText = "Matched in document content"
                )
            }
        }

        results.addAll(documents)

        // Add results for other types (FAQs, Guides, Lawyers)
        // ... Add similar queries for other collections

        return results
    }

    suspend fun getSearchHistory(): List<String> {
        val historyJson = context.dataStore.data.firstOrNull()?.get(SEARCH_HISTORY_KEY)
        return historyJson?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    suspend fun saveSearchHistory(history: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[SEARCH_HISTORY_KEY] = history.joinToString(",")
        }
    }

    suspend fun clearSearchHistory() {
        context.dataStore.edit { preferences ->
            preferences.remove(SEARCH_HISTORY_KEY)
        }
    }

    private fun getIconForDocumentType(type: DocumentType): Int {
        return when (type) {
            DocumentType.PDF -> R.drawable.ic_pdf
            DocumentType.WORD -> R.drawable.ic_word
            DocumentType.EXCEL -> R.drawable.ic_excel
            DocumentType.IMAGE -> R.drawable.ic_image
            DocumentType.TEXT -> R.drawable.ic_text
            DocumentType.OTHER -> R.drawable.ic_file
        }
    }
}