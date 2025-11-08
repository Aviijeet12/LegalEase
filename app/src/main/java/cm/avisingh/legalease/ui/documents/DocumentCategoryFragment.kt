package cm.avisingh.legalease.ui.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.FragmentDocumentCategoryBinding
import com.google.android.material.snackbar.Snackbar

class DocumentCategoryFragment : Fragment() {
    private var _binding: FragmentDocumentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var documentsAdapter: DocumentAdapter
    private lateinit var category: DocumentCategory
    private var isSelectionMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocumentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category = arguments?.getSerializable(ARG_CATEGORY) as DocumentCategory

        setupRecyclerView()
        loadDocuments()
    }

    private fun setupRecyclerView() {
        documentsAdapter = DocumentAdapter(
            onItemClick = { document ->
                if (isSelectionMode) {
                    document.isSelected = !document.isSelected
                    documentsAdapter.notifyItemChanged(documentsAdapter.currentList.indexOf(document))
                    updateSelectionUI()
                } else {
                    openDocument(document)
                }
            },
            onMoreClick = { document, view ->
                showDocumentOptions(document, view)
            }
        )

        binding.documentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = documentsAdapter
            itemAnimator?.apply {
                addDuration = 200
                moveDuration = 200
                changeDuration = 200
                removeDuration = 200
            }
        }
    }

    private fun loadDocuments() {
        // Show loading state
        binding.loadingIndicator.isVisible = true
        binding.documentsRecyclerView.isVisible = false
        binding.emptyStateLayout.isVisible = false

        // Simulated delay (will be replaced with actual API call)
        view?.postDelayed({
            val documents = getSampleDocuments()
            updateUI(documents)
        }, 1000)
    }

    private fun updateUI(documents: List<Document>) {
        binding.apply {
            loadingIndicator.isVisible = false
            documentsRecyclerView.isVisible = documents.isNotEmpty()
            emptyStateLayout.isVisible = documents.isEmpty()

            if (documents.isNotEmpty()) {
                documentsAdapter.submitList(documents)
            }
        }
    }

    private fun getSampleDocuments(): List<Document> {
        return when (category) {
            DocumentCategory.ALL -> listOf(
                Document(
                    "1",
                    "Sale Deed.pdf",
                    "Property sale agreement document",
                    "PDF",
                    "2.5 MB",
                    "Yesterday",
                    DocumentCategory.PROPERTY,
                    R.drawable.ic_pdf
                ),
                Document(
                    "2",
                    "Bank Statement.pdf",
                    "Monthly bank statement",
                    "PDF",
                    "1.2 MB",
                    "2 days ago",
                    DocumentCategory.FINANCIAL,
                    R.drawable.ic_pdf
                ),
                // Add more sample documents
            )
            else -> emptyList() // Filter by category
        }
    }

    fun sortDocuments(sortType: SortType) {
        val currentList = documentsAdapter.currentList.toMutableList()
        val sortedList = when (sortType) {
            SortType.NAME -> currentList.sortedBy { it.name }
            SortType.DATE -> currentList.sortedByDescending { it.date }
            SortType.SIZE -> currentList.sortedByDescending { it.size }
        }
        documentsAdapter.submitList(sortedList)
    }

    fun toggleSelectionMode() {
        isSelectionMode = !isSelectionMode
        documentsAdapter.setSelectionMode(isSelectionMode)
        updateSelectionUI()
    }

    private fun updateSelectionUI() {
        val selectedCount = documentsAdapter.currentList.count { it.isSelected }
        if (selectedCount > 0) {
            (activity as? DocumentsActivity)?.showDeleteConfirmation(selectedCount)
        }
    }

    fun deleteSelectedDocuments() {
        val selectedDocs = documentsAdapter.currentList.filter { it.isSelected }
        val remainingDocs = documentsAdapter.currentList.filterNot { it.isSelected }
        documentsAdapter.submitList(remainingDocs)
        
        Snackbar.make(
            binding.root,
            "${selectedDocs.size} documents deleted",
            Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            documentsAdapter.submitList(documentsAdapter.currentList + selectedDocs)
        }.show()

        isSelectionMode = false
        documentsAdapter.setSelectionMode(false)
    }

    private fun openDocument(document: Document) {
        // Open document in viewer
    }

    private fun showDocumentOptions(document: Document, anchorView: View) {
        // Show options menu
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: DocumentCategory): DocumentCategoryFragment {
            return DocumentCategoryFragment().apply {
                arguments = bundleOf(ARG_CATEGORY to category)
            }
        }
    }
}