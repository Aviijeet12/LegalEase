package com.nyayasetu.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.databinding.ItemRecentCaseBinding

class RecentCasesAdapter(private val cases: List<RecentCase>) :
    RecyclerView.Adapter<RecentCasesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecentCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(case: RecentCase) {
            binding.caseTitle.text = case.title
            binding.lawyerName.text = case.lawyer
            binding.caseStatus.text = case.status
            binding.caseTime.text = case.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentCaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cases[position])
    }

    override fun getItemCount(): Int = cases.size
}