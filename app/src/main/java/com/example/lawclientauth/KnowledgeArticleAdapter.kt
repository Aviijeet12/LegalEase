package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemKnowledgeArticleBinding

class KnowledgeArticleAdapter(
    private var list: List<KnowledgeArticleModel>,
    private val onClick: (KnowledgeArticleModel) -> Unit
) : RecyclerView.Adapter<KnowledgeArticleAdapter.AVH>() {

    inner class AVH(val b: ItemKnowledgeArticleBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AVH {
        val b = ItemKnowledgeArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AVH(b)
    }

    override fun onBindViewHolder(holder: AVH, pos: Int) {
        val a = list[pos]
        holder.b.tvTitle.text = a.title
        holder.b.tvSummary.text = a.summary
        holder.b.tvCategory.text = a.category

        holder.b.root.setOnClickListener { onClick(a) }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<KnowledgeArticleModel>) {
        list = newList
        notifyDataSetChanged()
    }
}
