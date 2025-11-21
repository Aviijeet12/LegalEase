package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemKnowledgeCategoryBinding

class KnowledgeCategoryAdapter(
    private var list: List<KnowledgeCategoryModel>,
    private val onClick: (KnowledgeCategoryModel) -> Unit
) : RecyclerView.Adapter<KnowledgeCategoryAdapter.CVH>() {

    inner class CVH(val b: ItemKnowledgeCategoryBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CVH {
        val binding = ItemKnowledgeCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CVH(binding)
    }

    override fun onBindViewHolder(holder: CVH, pos: Int) {
        val item = list[pos]
        holder.b.tvTitle.text = item.title
        holder.b.ivIcon.setImageResource(item.iconRes)

        holder.b.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = list.size
}
