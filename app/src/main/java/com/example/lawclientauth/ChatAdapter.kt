package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemMessageFileInBinding
import com.example.lawclientauth.databinding.ItemMessageFileOutBinding
import com.example.lawclientauth.databinding.ItemMessageImageInBinding
import com.example.lawclientauth.databinding.ItemMessageImageOutBinding
import com.example.lawclientauth.databinding.ItemMessageInBinding
import com.example.lawclientauth.databinding.ItemMessageOutBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val items: List<MessageModel>,
    private val onOpenFile: (String) -> Unit,
    private val onOpenImage: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_OUT_TEXT = 0
        private const val TYPE_IN_TEXT = 1
        private const val TYPE_OUT_FILE = 2
        private const val TYPE_IN_FILE = 3
        private const val TYPE_OUT_IMAGE = 4
        private const val TYPE_IN_IMAGE = 5
    }

    override fun getItemViewType(position: Int): Int {
        val m = items[position]
        val isMe = m.senderId == "me"
        return when (m.type) {
            "text" -> if (isMe) TYPE_OUT_TEXT else TYPE_IN_TEXT
            "file" -> if (isMe) TYPE_OUT_FILE else TYPE_IN_FILE
            "image" -> if (isMe) TYPE_OUT_IMAGE else TYPE_IN_IMAGE
            else -> if (isMe) TYPE_OUT_TEXT else TYPE_IN_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_OUT_TEXT -> OutTextVH(ItemMessageOutBinding.inflate(inf, parent, false))
            TYPE_IN_TEXT -> InTextVH(ItemMessageInBinding.inflate(inf, parent, false))
            TYPE_OUT_FILE -> OutFileVH(ItemMessageFileOutBinding.inflate(inf, parent, false))
            TYPE_IN_FILE -> InFileVH(ItemMessageFileInBinding.inflate(inf, parent, false))
            TYPE_OUT_IMAGE -> OutImageVH(ItemMessageImageOutBinding.inflate(inf, parent, false))
            else -> InImageVH(ItemMessageImageInBinding.inflate(inf, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val m = items[position]
        val time = timeString(m.timestamp)

        when (holder) {
            is OutTextVH -> {
                holder.b.tvMessage.text = m.text
                holder.b.tvTime.text = time
                holder.b.tvStatus.text = statusToTick(m.status)
            }
            is InTextVH -> {
                holder.b.tvMessage.text = m.text
                holder.b.tvTime.text = time
            }
            is OutFileVH -> {
                holder.b.tvFileName.text = m.text ?: "Document"
                holder.b.tvTime.text = time
                holder.b.tvStatus.text = statusToTick(m.status)
                holder.b.btnOpen.setOnClickListener { m.fileUrl?.let { onOpenFile(it) } }
            }
            is InFileVH -> {
                holder.b.tvFileName.text = m.text ?: "Document"
                holder.b.tvTime.text = time
                holder.b.btnOpen.setOnClickListener { m.fileUrl?.let { onOpenFile(it) } }
            }
            is OutImageVH -> {
                holder.b.tvFileName.text = m.text ?: "Image"
                holder.b.tvTime.text = time
                holder.b.tvStatus.text = statusToTick(m.status)
                holder.b.imgThumb.setOnClickListener { m.fileUrl?.let { onOpenImage(it) } }
            }
            is InImageVH -> {
                holder.b.tvFileName.text = m.text ?: "Image"
                holder.b.tvTime.text = time
                holder.b.imgThumb.setOnClickListener { m.fileUrl?.let { onOpenImage(it) } }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    private fun timeString(ts: Long): String {
        val fmt = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return fmt.format(Date(ts))
    }

    private fun statusToTick(status: String): String {
        return when (status) {
            "sending" -> "⏳"
            "sent" -> "✓"
            "delivered" -> "✓✓"
            "read" -> "✓✓ (read)"
            else -> ""
        }
    }

    class OutTextVH(val b: ItemMessageOutBinding) : RecyclerView.ViewHolder(b.root)
    class InTextVH(val b: ItemMessageInBinding) : RecyclerView.ViewHolder(b.root)
    class OutFileVH(val b: ItemMessageFileOutBinding) : RecyclerView.ViewHolder(b.root)
    class InFileVH(val b: ItemMessageFileInBinding) : RecyclerView.ViewHolder(b.root)
    class OutImageVH(val b: ItemMessageImageOutBinding) : RecyclerView.ViewHolder(b.root)
    class InImageVH(val b: ItemMessageImageInBinding) : RecyclerView.ViewHolder(b.root)
}
