package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemTimelineBinding

class TimelineAdapter(private val items: List<TimelineModel>) :
    RecyclerView.Adapter<TimelineAdapter.TimelineVH>() {

    inner class TimelineVH(val binding: ItemTimelineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineVH {
        val b = ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimelineVH(b)
    }

    override fun onBindViewHolder(holder: TimelineVH, position: Int) {
        val t = items[position]
        holder.binding.tvEvent.text = t.title
        holder.binding.tvDate.text = t.date
    }

    override fun getItemCount(): Int = items.size
}
