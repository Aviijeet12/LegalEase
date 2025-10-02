package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R

data class Conversation(
    val id: String = "",
    val name: String = "",
    val lastMessage: String = "",
    val timestamp: String = "",
    val unreadCount: Int = 0
)

class ConversationsAdapter(
    private var conversations: List<Conversation>,
    private val onConversationClick: (Conversation) -> Unit = {}
) : RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.bind(conversation)
        holder.itemView.setOnClickListener { onConversationClick(conversation) }
    }

    override fun getItemCount() = conversations.size

    fun updateData(newConversations: List<Conversation>) {
        conversations = newConversations
        notifyDataSetChanged()
    }

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.itemTitle)
        private val messageText: TextView = itemView.findViewById(R.id.itemDescription)

        fun bind(conversation: Conversation) {
            nameText.text = conversation.name
            messageText.text = conversation.lastMessage
        }
    }
}