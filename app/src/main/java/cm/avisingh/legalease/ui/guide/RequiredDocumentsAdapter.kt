package cm.avisingh.legalease.ui.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemRequiredDocumentBinding

class RequiredDocumentsAdapter(
    private val onSampleClick: (RequiredDocument) -> Unit
) : ListAdapter<RequiredDocument, RequiredDocumentsAdapter.ViewHolder>(DocumentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRequiredDocumentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onSampleClick)
    }

    class ViewHolder(
        private val binding: ItemRequiredDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(document: RequiredDocument, onSampleClick: (RequiredDocument) -> Unit) {
            binding.apply {
                documentIcon.setImageResource(document.iconResId)
                documentNameText.text = document.name
                sampleButton.setOnClickListener { onSampleClick(document) }
            }
        }
    }
}

private class DocumentDiffCallback : DiffUtil.ItemCallback<RequiredDocument>() {
    override fun areItemsTheSame(oldItem: RequiredDocument, newItem: RequiredDocument): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RequiredDocument, newItem: RequiredDocument): Boolean {
        return oldItem == newItem
    }
}