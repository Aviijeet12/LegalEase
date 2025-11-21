package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemLawyerCaseBinding

class LawyerCasesAdapter(
    private var list: List<CaseModel>,
    private val onOpen: (CaseModel) -> Unit,
    private val onViewDoc: (CaseModel) -> Unit,
    private val onChangeStatus: (CaseModel, String) -> Unit
) : RecyclerView.Adapter<LawyerCasesAdapter.CaseVH>() {

    inner class CaseVH(val binding: ItemLawyerCaseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseVH {
        val binding = ItemLawyerCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CaseVH(binding)
    }

    override fun onBindViewHolder(holder: CaseVH, position: Int) {
        val c = list[position]
        holder.binding.tvTitle.text = c.title
        holder.binding.tvCaseId.text = c.caseId
        holder.binding.tvStatus.text = c.status
        holder.binding.tvClient.text = c.lawyerName
        holder.binding.tvLastUpdated.text = "Last: ${c.lastUpdated}"

        holder.binding.btnOpen.setOnClickListener { onOpen(c) }
        holder.binding.btnViewDoc.setOnClickListener { onViewDoc(c) }

        holder.binding.btnChangeStatus.setOnClickListener {
            // Simple cycle of statuses for UI demo
            val next = when (c.status) {
                "Open" -> "Hearing Soon"
                "Hearing Soon" -> "Closed"
                "Documents Pending" -> "Open"
                "Closed" -> "Open"
                else -> "Open"
            }
            onChangeStatus(c, next)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<CaseModel>) {
        this.list = newList
        notifyDataSetChanged()
    }
}
