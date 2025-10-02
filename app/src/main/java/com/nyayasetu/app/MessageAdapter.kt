package com.nyayasetu.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.databinding.ItemMessageBinding

class MessageAdapter(private var messages: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageText.text = message.text
            binding.timestamp.text = message.timestamp

            if (message.isSender) {
                binding.messageCard.setCardBackgroundColor(0xFFE3F2FD.toInt())
                binding.messageText.setTextColor(0xFF000000.toInt())
            } else {
                binding.messageCard.setCardBackgroundColor(0xFFF5F5F5.toInt())
                binding.messageText.setTextColor(0xFF000000.toInt())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}