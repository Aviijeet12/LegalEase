package com.nyayasetu.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R

class CaseAdapter(private val cases: List<Map<String, Any>>) : RecyclerView.Adapter<CaseAdapter.CaseViewHolder>() {
    class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val caseTitle: TextView = itemView.findViewById(R.id.textViewCaseTitle)
        val caseDesc: TextView = itemView.findViewById(R.id.textViewCaseDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return CaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val case = cases[position]
        holder.caseTitle.text = case["title"].toString()
        holder.caseDesc.text = case["description"].toString()
    }

    override fun getItemCount(): Int = cases.size
}
