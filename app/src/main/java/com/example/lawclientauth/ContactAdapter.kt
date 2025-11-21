package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemContactBinding

class ContactAdapter(
    private val list: List<ContactModel>,
    private val onChat: (ContactModel) -> Unit,
    private val onCall: (ContactModel) -> Unit,
    private val onVideoCall: (ContactModel) -> Unit
) : RecyclerView.Adapter<ContactAdapter.CVH>() {

    inner class CVH(val b: ItemContactBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CVH {
        return CVH(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CVH, pos: Int) {
        val contact = list[pos]

        holder.b.tvName.text = contact.name
        holder.b.tvLastMessage.text = contact.lastMessage
        holder.b.tvRole.text = contact.role.uppercase()

        // Role badge colors
        if (contact.role == "lawyer") {
            holder.b.tvRole.setBackgroundResource(R.drawable.bg_badge_lawyer)
        } else {
            holder.b.tvRole.setBackgroundResource(R.drawable.bg_badge_client)
        }

        // Online indicator
        if (contact.isOnline) {
            holder.b.onlineIndicator.setImageResource(R.drawable.ic_online)
        } else {
            holder.b.onlineIndicator.setImageResource(R.drawable.ic_offline)
        }

        // Buttons
        holder.b.btnChat.setOnClickListener { onChat(contact) }
        holder.b.btnCall.setOnClickListener { onCall(contact) }
        holder.b.btnVideoCall.setOnClickListener { onVideoCall(contact) }
    }

    override fun getItemCount(): Int = list.size
}
