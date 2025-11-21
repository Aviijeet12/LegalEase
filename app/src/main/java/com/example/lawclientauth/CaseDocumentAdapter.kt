package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemCaseDocumentBinding

data class CaseDocumentModel(val name: String, val url: String)

class CaseDocumentAdapter(
    private var list: List<CaseDocumentModel>,
    private val onClick: (CaseDocumentModel) -> Unit
) : RecyclerView.Adapter<CaseDocumentAdapter.VH>() {

    inner class VH(val b: ItemCaseDocumentBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemCaseDocumentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val doc = list[position]
        holder.b.tvDocName.text = doc.name
        holder.b.btnOpenDoc.setOnClickListener { onClick(doc) }
    }

    override fun getItemCount(): Int = list.size
}
