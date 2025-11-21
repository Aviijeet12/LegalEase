package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemCallBinding

class CallAdapter(private val list: List<CallModel>, private val onClick: (CallModel) -> Unit) :
    RecyclerView.Adapter<CallAdapter.CallVH>() {

    inner class CallVH(val binding: ItemCallBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallVH {
        val b = ItemCallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallVH(b)
    }

    override fun onBindViewHolder(holder: CallVH, position: Int) {
        val c = list[position]
        holder.binding.tvPeer.text = c.peerName
        holder.binding.tvType.text = c.type.capitalize()
        holder.binding.tvStatus.text = c.status.capitalize()
        holder.binding.tvTime.text = java.text.SimpleDateFormat("dd MMM, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(c.timestamp))
        holder.binding.root.setOnClickListener { onClick(c) }
    }

    override fun getItemCount(): Int = list.size
}
