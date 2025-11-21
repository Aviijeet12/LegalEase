package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemAppointmentBinding

class AppointmentAdapter(
    private var list: List<AppointmentModel>,
    private val onClick: (AppointmentModel) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppVH>() {

    inner class AppVH(val binding: ItemAppointmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppVH {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppVH(binding)
    }

    override fun onBindViewHolder(holder: AppVH, position: Int) {
        val a = list[position]
        holder.binding.tvTitle.text = a.title
        holder.binding.tvLawyer.text = a.lawyerName
        holder.binding.tvTime.text = a.time
        holder.binding.tvStatus.text = a.status

        holder.binding.btnChat.setOnClickListener {
            onClick(a) // could open chat
        }

        holder.binding.btnViewDoc.setOnClickListener {
            a.documentUrl?.let { url ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(url), "*/*")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    holder.binding.root.context.startActivity(intent)
                } catch (e: Exception) {
                    // ignore for now
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<AppointmentModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}
