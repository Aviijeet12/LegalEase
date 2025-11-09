package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemDocumentBinding
import cm.avisingh.legalease.models.UserDocument
import java.text.SimpleDateFormat
import java.util.*

class DocumentsAdapter(
    private val documents: List<UserDocument>,
    private val onDocumentClick: (UserDocument) -> Unit
) : RecyclerView.Adapter<DocumentsAdapter.DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = ItemDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(documents[position])
    }

    override fun getItemCount() = documents.size

    inner class DocumentViewHolder(
        private val binding: ItemDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(document: UserDocument) {
            binding.documentNameText.text = document.title
            binding.documentInfoText.text = "${document.documentType} • ${formatFileSize(document.fileSize)} • ${formatDate(document.uploadedAt)}"

            binding.root.setOnClickListener {
                onDocumentClick(document)
            }
            
            binding.moreButton.setOnClickListener {
                onDocumentClick(document)
            }
        }

        private fun formatFileSize(size: Long): String {
            val kb = size / 1024.0
            val mb = kb / 1024.0
            return when {
                mb >= 1 -> String.format("%.2f MB", mb)
                kb >= 1 -> String.format("%.2f KB", kb)
                else -> "$size bytes"
            }
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}
