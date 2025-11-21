package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemAppointmentRequestBinding

class AppointmentRequestAdapter(
    private var list: List<AppointmentRequestModel>,
    private val onAccept: (AppointmentRequestModel) -> Unit,
    private val onReject: (AppointmentRequestModel) -> Unit,
    private val onViewClient: (AppointmentRequestModel) -> Unit
) : RecyclerView.Adapter<AppointmentRequestAdapter.RQVH>() {

    inner class RQVH(val binding: ItemAppointmentRequestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RQVH {
        val b = ItemAppointmentRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RQVH(b)
    }

    override fun onBindViewHolder(holder: RQVH, position: Int) {
        val r = list[position]
        holder.binding.tvClient.text = r.clientName
        holder.binding.tvDate.text = r.date
        holder.binding.tvTime.text = r.time
        holder.binding.tvType.text = r.meetingType
        holder.binding.tvReason.text = r.reason
        holder.binding.tvStatus.text = r.status

        holder.binding.btnAccept.setOnClickListener { onAccept(r) }
        holder.binding.btnReject.setOnClickListener { onReject(r) }
        holder.binding.btnViewClient.setOnClickListener { onViewClient(r) }

        holder.binding.btnViewDocs.visibility = if (r.clientDocs.isNotEmpty()) View.VISIBLE else View.GONE
        holder.binding.btnViewDocs.setOnClickListener {
            // open first doc using intent (UI only); use local path
            val url = r.clientDocs.firstOrNull()
            url?.let {
                try {
                    val i = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    i.setDataAndType(android.net.Uri.parse(it), "*/*")
                    holder.binding.root.context.startActivity(i)
                } catch (e: Exception) { }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<AppointmentRequestModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}
