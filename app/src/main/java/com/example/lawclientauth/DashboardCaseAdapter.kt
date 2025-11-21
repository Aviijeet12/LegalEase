package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.DashboardCaseItemBinding

class DashboardCaseAdapter(private val list: List<DashboardCaseModel>) :
    RecyclerView.Adapter<DashboardCaseAdapter.VH>() {

    inner class VH(val b: DashboardCaseItemBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            DashboardCaseItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val case = list[position]
        holder.b.tvCaseTitle.text = case.title
        holder.b.tvCaseType.text = case.type
        holder.b.tvCaseStatus.text = case.status
    }

    override fun getItemCount(): Int = list.size
}
