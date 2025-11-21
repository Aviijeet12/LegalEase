package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemAdminOptionBinding

class AdminOptionAdapter(
    private val list: List<AdminOptionModel>,
    private val onClick: (AdminOptionModel) -> Unit
) : RecyclerView.Adapter<AdminOptionAdapter.AVH>() {

    inner class AVH(val b: ItemAdminOptionBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AVH {
        val b = ItemAdminOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AVH(b)
    }

    override fun onBindViewHolder(holder: AVH, position: Int) {
        val item = list[position]

        holder.b.tvTitle.text = item.title
        holder.b.ivIcon.setImageResource(item.iconRes)

        holder.b.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = list.size
}
