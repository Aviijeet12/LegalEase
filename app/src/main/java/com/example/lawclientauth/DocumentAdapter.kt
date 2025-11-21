package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemDocumentBinding

class DocumentAdapter(
    private val list: List<DocumentModel>,
    private val onClick: (DocumentModel) -> Unit
) : RecyclerView.Adapter<DocumentAdapter.DocVH>() {

    inner class DocVH(val binding: ItemDocumentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocVH {
        val binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocVH(binding)
    }

    override fun onBindViewHolder(holder: DocVH, position: Int) {
        val item = list[position]

        holder.binding.tvDocTitle.text = item.title
        holder.binding.tvDocType.text = item.type
        holder.binding.tvDocDate.text = item.date

        holder.binding.btnOpenDoc.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = list.size
}
