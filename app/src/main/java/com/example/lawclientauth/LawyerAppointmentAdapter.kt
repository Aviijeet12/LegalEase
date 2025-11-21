package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemLawyerAppointmentBinding

class LawyerAppointmentAdapter(
    private var list: List<AppointmentModel>,
    private val onJoinCall: (AppointmentModel) -> Unit,
    private val onMarkComplete: (AppointmentModel) -> Unit,
    private val onCancel: (AppointmentModel) -> Unit
) : RecyclerView.Adapter<LawyerAppointmentAdapter.VH>() {

    inner class VH(val b: ItemLawyerAppointmentBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemLawyerAppointmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val a = list[position]

        holder.b.tvClientName.text = a.clientName
        holder.b.tvPurpose.text = a.purpose
        holder.b.tvDate.text = a.date
        holder.b.tvTime.text = a.time
        holder.b.tvMeetingType.text = a.meetingType
        holder.b.tvStatus.text = a.status

        holder.b.btnJoinCall.setOnClickListener { onJoinCall(a) }
        holder.b.btnMarkComplete.setOnClickListener { onMarkComplete(a) }
        holder.b.btnCancel.setOnClickListener { onCancel(a) }
    }

    fun updateList(newList: List<AppointmentModel>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}
