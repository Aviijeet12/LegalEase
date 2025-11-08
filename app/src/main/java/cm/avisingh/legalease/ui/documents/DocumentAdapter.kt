package cm.avisingh.legalease.ui.documents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemDocumentBinding

class DocumentAdapter(
    private val onItemClick: (Document) -> Unit,
    private val onMoreClick: (Document, View) -> Unit
) : ListAdapter<Document, DocumentAdapter.ViewHolder>(DocumentDiffCallback()) {

    private var selectionMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDocumentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), selectionMode, onItemClick, onMoreClick)
    }

    fun setSelectionMode(enabled: Boolean) {
        selectionMode = enabled
        if (!enabled) {
            // Clear selection when exiting selection mode
            currentList.forEach { it.isSelected = false }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            document: Document,
            selectionMode: Boolean,
            onItemClick: (Document) -> Unit,
            onMoreClick: (Document, View) -> Unit
        ) {
            binding.apply {
                documentIcon.setImageResource(document.iconResId)
                documentNameText.text = document.name
                documentInfoText.text = "${document.type} • ${document.size} • ${document.date}"

                // Handle selection state
                root.isSelected = document.isSelected
                root.alpha = if (selectionMode && !document.isSelected) 0.5f else 1.0f

                // Click listeners
                root.setOnClickListener { onItemClick(document) }
                moreButton.setOnClickListener { onMoreClick(document, it) }
            }
        }
    }
}

private class DocumentDiffCallback : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem == newItem
    }
}