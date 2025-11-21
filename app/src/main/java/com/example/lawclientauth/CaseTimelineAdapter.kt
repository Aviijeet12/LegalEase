package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemCaseTimelineBinding

data class CaseTimelineModel(val title: String, val date: String)

class CaseTimelineAdapter(private val list: List<CaseTimelineModel>) :
    RecyclerView.Adapter<CaseTimelineAdapter.VH>() {

    inner class VH(val b: ItemCaseTimelineBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemCaseTimelineBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.b.tvTimelineTitle.text = item.title
        holder.b.tvTimelineDate.text = item.date
    }

    override fun getItemCount(): Int = list.size
}
