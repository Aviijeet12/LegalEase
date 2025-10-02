package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R
import com.nyayasetu.app.models.Document

class CaseDocumentsAdapter(
    private var documents: List<Document>,
    private val onDocumentClick: (Document) -> Unit = {}
) : RecyclerView.Adapter<CaseDocumentsAdapter.DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]
        holder.bind(document)
        holder.itemView.setOnClickListener { onDocumentClick(document) }
    }

    override fun getItemCount() = documents.size

    fun updateData(newDocuments: List<Document>) {
        documents = newDocuments
        notifyDataSetChanged()
    }

    class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.documentTitle)
        private val typeText: TextView = itemView.findViewById(R.id.documentType)
        private val sizeText: TextView = itemView.findViewById(R.id.documentSize)

        fun bind(document: Document) {
            titleText.text = document.title
            typeText.text = document.type
            sizeText.text = document.size
        }
    }
}