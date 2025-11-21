package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemReportBinding
import java.text.SimpleDateFormat
import java.util.*

class ReportAdapter(
    private var list: List<ReportModel>,
    private val onClick: (ReportModel) -> Unit
) : RecyclerView.Adapter<ReportAdapter.RVH>() {

    inner class RVH(val b: ItemReportBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVH {
        return RVH(
            ItemReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RVH, position: Int) {
        val report = list[position]

        holder.b.tvTitle.text = report.title
        holder.b.tvFiledBy.text = report.filedBy
        holder.b.tvCategory.text = report.category
        holder.b.tvTime.text = formatTime(report.time)

        if (report.status == "Resolved") {
            holder.b.tvStatus.text = "Resolved"
            holder.b.tvStatus.setBackgroundResource(R.drawable.bg_status_resolved)
        } else {
            holder.b.tvStatus.text = "Pending"
            holder.b.tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
        }

        holder.b.root.setOnClickListener {
            onClick(report)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<ReportModel>) {
        list = newList
        notifyDataSetChanged()
    }

    private fun formatTime(ts: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(ts))
    }
}
