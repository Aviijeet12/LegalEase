package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R
import com.nyayasetu.app.models.Case
import java.text.SimpleDateFormat
import java.util.*

class AdminCasesAdapter(
    private var cases: List<Case>,
    private val onCaseClick: (Case) -> Unit
) : RecyclerView.Adapter<AdminCasesAdapter.CaseViewHolder>() {

    inner class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewCaseTitle: TextView = itemView.findViewById(R.id.textViewCaseTitle)
        private val textViewCaseNumber: TextView = itemView.findViewById(R.id.textViewCaseNumber)
        private val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewClient: TextView = itemView.findViewById(R.id.textViewClient)

        fun bind(case: Case) {
            textViewCaseTitle.text = case.title
            textViewCaseNumber.text = "Case #${case.caseNumber}"
            textViewStatus.text = case.status
            
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            textViewDate.text = dateFormat.format(Date(case.createdAt))
            
            textViewClient.text = "Client: ${case.clientName}"
            
            itemView.setOnClickListener { onCaseClick(case) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_case, parent, false)
        return CaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(cases[position])
    }

    override fun getItemCount(): Int = cases.size

    fun updateCases(newCases: List<Case>) {
        cases = newCases
        notifyDataSetChanged()
    }
}