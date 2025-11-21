package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemCallLogBinding
import java.text.SimpleDateFormat
import java.util.*

class CallLogAdapter(
    private val list: List<CallLogModel>
) : RecyclerView.Adapter<CallLogAdapter.CVH>() {

    inner class CVH(val b: ItemCallLogBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CVH {
        return CVH(
            ItemCallLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CVH, position: Int) {
        val item = list[position]

        holder.b.tvName.text = item.name

        holder.b.tvTime.text = timeFormatter(item.time)

        // Duration formatting
        if (item.status == "missed") {
            holder.b.tvDuration.text = "Missed"
            holder.b.tvDuration.setTextColor(0xFFD32F2F.toInt()) // Red
        } else {
            holder.b.tvDuration.setTextColor(0xFF555555.toInt())
            holder.b.tvDuration.text = durationFormatter(item.duration)
        }

        // Call type icon
        when (item.type) {
            "audio" -> holder.b.ivType.setImageResource(R.drawable.ic_audio_call)
            "video" -> holder.b.ivType.setImageResource(R.drawable.ic_video_call)
        }

        // Incoming/outgoing arrows
        when (item.status) {
            "incoming" -> holder.b.ivDirection.setImageResource(R.drawable.ic_arrow_in)
            "outgoing" -> holder.b.ivDirection.setImageResource(R.drawable.ic_arrow_out)
            "missed" -> holder.b.ivDirection.setImageResource(R.drawable.ic_arrow_missed)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun timeFormatter(ts: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(ts))
    }

    private fun durationFormatter(seconds: Long): String {
        val min = seconds / 60
        val sec = seconds % 60
        return "${min}m ${sec}s"
    }
}

