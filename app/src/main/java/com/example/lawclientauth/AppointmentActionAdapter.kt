package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemAppointmentActionBinding

class AppointmentActionAdapter(
    private var list: List<AppointmentModel>
) : RecyclerView.Adapter<AppointmentActionAdapter.AppVH>() {

    inner class AppVH(val binding: ItemAppointmentActionBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppVH {
        val binding = ItemAppointmentActionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppVH(binding)
    }

    override fun onBindViewHolder(holder: AppVH, position: Int) {
        val a = list[position]

        holder.binding.tvTitle.text = a.title
        holder.binding.tvTime.text = a.time
        holder.binding.tvLawyer.text = a.lawyerName
        holder.binding.tvStatus.text = a.status

        // Status badge color
        when (a.status) {
            "Upcoming" -> holder.binding.tvStatus.setBackgroundColor(0xFF0B2F4E.toInt())
            "Completed" -> holder.binding.tvStatus.setBackgroundColor(0xFF4CAF50.toInt())
            "Cancelled" -> holder.binding.tvStatus.setBackgroundColor(0xFFF44336.toInt())
        }

        holder.binding.btnJoinCall.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Join call (frontend only)",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.binding.btnChat.setOnClickListener {
            holder.binding.root.context.startActivity(
                Intent(holder.binding.root.context, ChatActivity::class.java)
            )
        }

        holder.binding.btnReschedule.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Reschedule UI coming in next features!",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.binding.btnCancel.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Appointment cancelled (UI only)",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.binding.btnViewDoc.setOnClickListener {
            a.documentUrl?.let { url ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(url), "*/*")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    holder.binding.root.context.startActivity(intent)
                } catch (e: Exception) { }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<AppointmentModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}
