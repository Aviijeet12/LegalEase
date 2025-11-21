package com.example.lawclientauth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.R
import com.example.lawclientauth.models.ItemModel

class ItemAdapter(
    private val fullList: MutableList<ItemModel>,
    private val onItemClick: (ItemModel) -> Unit,
    private val onItemLongClick: (ItemModel, Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var filteredList: MutableList<ItemModel> = fullList.toMutableList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tvItemTitle)
        val desc = view.findViewById<TextView>(R.id.tvItemDesc)
        val date = view.findViewById<TextView>(R.id.tvItemDate)

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    onItemClick(filteredList[adapterPosition])
            }
            view.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    onItemLongClick(filteredList[adapterPosition], adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = filteredList[position]

        holder.title.text = i.title
        holder.desc.text = i.description
        holder.date.text =
            android.text.format.DateFormat.format("dd MMM yyyy", i.createdAt)

        // Apply animation
        val anim = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_scale_anim)
        holder.itemView.startAnimation(anim)
    }

    fun setList(list: List<ItemModel>) {
        fullList.clear()
        fullList.addAll(list)
        filteredList = fullList.toMutableList()
        notifyDataSetChanged()
    }

    fun addMore(list: List<ItemModel>) {
        val start = fullList.size
        fullList.addAll(list)
        filteredList = fullList.toMutableList()
        notifyItemRangeInserted(start, list.size)
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            fullList.toMutableList()
        } else {
            val q = query.lowercase()
            fullList.filter {
                it.title.lowercase().contains(q) ||
                it.description.lowercase().contains(q)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
