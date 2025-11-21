package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.DashboardAppointmentItemBinding

class DashboardAppointmentAdapter(private val list: List<DashboardAppointmentModel>) :
    RecyclerView.Adapter<DashboardAppointmentAdapter.VH>() {

    inner class VH(val b: DashboardAppointmentItemBinding) :
        RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            DashboardAppointmentItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.b.tvAppTitle.text = item.title
        holder.b.tvAppTime.text = item.time
    }

    override fun getItemCount(): Int = list.size
}
