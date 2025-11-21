package com.example.lawclientauth

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemCaseBinding

class CaseAdapter(private val list: List<CaseModel>) :
    RecyclerView.Adapter<CaseAdapter.CaseViewHolder>() {

    inner class CaseViewHolder(val binding: ItemCaseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val binding = ItemCaseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvCaseTitle.text = item.title
        holder.binding.tvCaseId.text = item.caseId
        holder.binding.tvLawyerName.text = item.lawyerName
        holder.binding.tvLastUpdated.text = "Last Updated: ${item.lastUpdated}"

        // Color badge for status
        holder.binding.tvStatus.text = item.status

        when (item.status) {
            "Open" -> holder.binding.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"))
            "Closed" -> holder.binding.tvStatus.setBackgroundColor(Color.parseColor("#F44336"))
            "Pending" -> holder.binding.tvStatus.setBackgroundColor(Color.parseColor("#FFC107"))
        }

        holder.binding.btnViewDetails.setOnClickListener {
            // Later → navigate to Case Details Screen
        }
    }

    override fun getItemCount() = list.size
}
