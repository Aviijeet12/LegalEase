package com.example.lawclientauth

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val list: List<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.NVH>() {

    inner class NVH(val b: ItemNotificationBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NVH {
        return NVH(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NVH, pos: Int) {
        val n = list[pos]

        holder.b.tvTitle.text = n.title
        holder.b.tvMessage.text = n.message

        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        holder.b.tvTime.text = sdf.format(Date(n.timestamp))

        // unread indicator
        holder.b.unreadDot.setBackgroundColor(
            if (!n.isRead) Color.parseColor("#D4AF37") else Color.TRANSPARENT
        )

        // type badge color
        val typeColor = when (n.type) {
            "case" -> "#0B2F4E"
            "appointment" -> "#00796B"
            "payment" -> "#E65100"
            "chat" -> "#1565C0"
            else -> "#757575"
        }

        holder.b.tvBadge.setBackgroundColor(Color.parseColor(typeColor))
        holder.b.tvBadge.text = n.type.uppercase()

        holder.b.root.setOnClickListener {
            n.isRead = true
            notifyItemChanged(pos)
        }
    }

    override fun getItemCount() = list.size
}
