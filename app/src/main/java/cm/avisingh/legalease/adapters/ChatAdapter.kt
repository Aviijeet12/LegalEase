package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.models.Message
import cm.avisingh.legalease.utils.SharedPrefManager
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val messages: MutableList<Message>,
    private val sharedPrefManager: SharedPrefManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    // Sent Message ViewHolder
    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvSentMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvSentTime)

        fun bind(message: Message) {
            tvMessage.text = message.message
            tvTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.timestamp)
        }
    }

    // Received Message ViewHolder
    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvReceivedMessage)
        private val tvSender: TextView = itemView.findViewById(R.id.tvSenderName)
        private val tvTime: TextView = itemView.findViewById(R.id.tvReceivedTime)

        fun bind(message: Message) {
            tvMessage.text = message.message
            tvSender.text = message.senderName
            tvTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.timestamp)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentUserId = sharedPrefManager.getUserEmail() // Using email as ID for now
        return if (messages[position].senderId == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_sent, parent, false)
                SentMessageViewHolder(view)
            }
            VIEW_TYPE_RECEIVED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_received, parent, false)
                ReceivedMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SentMessageViewHolder -> holder.bind(messages[position])
            is ReceivedMessageViewHolder -> holder.bind(messages[position])
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun clearMessages() {
        messages.clear()
        notifyDataSetChanged()
    }
}