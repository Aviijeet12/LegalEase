package cm.avisingh.legalease.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.data.model.Document
import cm.avisingh.legalease.databinding.ItemRecentDocumentBinding
import java.text.SimpleDateFormat
import java.util.*

class RecentDocumentsAdapter(
    private val onDocumentClick: (Document) -> Unit
) : ListAdapter<Document, RecentDocumentsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemRecentDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(document: Document) {
            binding.apply {
                fileNameText.text = document.name
                fileSizeText.text = formatFileSize(document.size)
                lastModifiedText.text = formatLastModified(document.updatedAt.toDate())

                // Set file type icon based on extension
                fileTypeIcon.setImageResource(getFileTypeIcon(document.name))

                root.setOnClickListener { onDocumentClick(document) }
            }
        }

        private fun formatFileSize(size: Long): String {
            val kb = size / 1024.0
            return when {
                kb < 1024 -> String.format("%.1f KB", kb)
                kb < 1024 * 1024 -> String.format("%.1f MB", kb / 1024)
                else -> String.format("%.1f GB", kb / (1024 * 1024))
            }
        }

        private fun formatLastModified(date: Date): String {
            val now = System.currentTimeMillis()
            val diff = now - date.time

            return when {
                diff < 60 * 60 * 1000 -> // Less than 1 hour
                    "Modified ${diff / (60 * 1000)}m ago"
                diff < 24 * 60 * 60 * 1000 -> // Less than 24 hours
                    "Modified ${diff / (60 * 60 * 1000)}h ago"
                else -> "Modified ${SimpleDateFormat("MMM d", Locale.getDefault()).format(date)}"
            }
        }

        private fun getFileTypeIcon(fileName: String): Int {
            return when (fileName.substringAfterLast('.', "").toLowerCase(Locale.ROOT)) {
                "pdf" -> R.drawable.ic_pdf
                "doc", "docx" -> R.drawable.ic_word
                "xls", "xlsx" -> R.drawable.ic_excel
                "ppt", "pptx" -> R.drawable.ic_powerpoint
                "jpg", "jpeg", "png" -> R.drawable.ic_image
                else -> R.drawable.ic_document
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Document>() {
            override fun areItemsTheSame(oldItem: Document, newItem: Document) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Document, newItem: Document) =
                oldItem == newItem
        }
    }
}