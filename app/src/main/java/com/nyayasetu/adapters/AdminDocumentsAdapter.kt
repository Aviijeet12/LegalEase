package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R
import com.nyayasetu.app.models.Document
import java.text.SimpleDateFormat
import java.util.*

class AdminDocumentsAdapter(
    private var documents: List<Document>,
    private val onDocumentClick: (Document) -> Unit,
    private val onDownloadClick: (Document) -> Unit
) : RecyclerView.Adapter<AdminDocumentsAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewFileName: TextView = itemView.findViewById(R.id.textViewFileName)
        private val textViewFileType: TextView = itemView.findViewById(R.id.textViewFileType)
        private val textViewUploadDate: TextView = itemView.findViewById(R.id.textViewUploadDate)
        private val textViewUploadedBy: TextView = itemView.findViewById(R.id.textViewUploadedBy)
        private val imageViewFileIcon: ImageView = itemView.findViewById(R.id.imageViewFileIcon)
        private val buttonDownload: View = itemView.findViewById(R.id.buttonDownload)

        fun bind(document: Document) {
            textViewFileName.text = document.fileName
            textViewFileType.text = document.fileType.uppercase()
            
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            textViewUploadDate.text = dateFormat.format(Date(document.uploadDate))
            
            textViewUploadedBy.text = "By: ${document.uploadedBy}"
            
            // Set appropriate icon based on file type
            when (document.fileType.lowercase()) {
                "pdf" -> imageViewFileIcon.setImageResource(android.R.drawable.ic_menu_agenda)
                "doc", "docx" -> imageViewFileIcon.setImageResource(android.R.drawable.ic_menu_edit)
                "jpg", "jpeg", "png" -> imageViewFileIcon.setImageResource(android.R.drawable.ic_menu_gallery)
                else -> imageViewFileIcon.setImageResource(android.R.drawable.ic_menu_info_details)
            }
            
            itemView.setOnClickListener { onDocumentClick(document) }
            buttonDownload.setOnClickListener { onDownloadClick(document) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(documents[position])
    }

    override fun getItemCount(): Int = documents.size

    fun updateDocuments(newDocuments: List<Document>) {
        documents = newDocuments
        notifyDataSetChanged()
    }
}