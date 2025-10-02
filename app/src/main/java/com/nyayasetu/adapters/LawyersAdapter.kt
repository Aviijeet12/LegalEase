package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R
import com.nyayasetu.app.models.User

class LawyersAdapter(
    private var lawyers: List<User>,
    private val onLawyerClick: (User) -> Unit
) : RecyclerView.Adapter<LawyersAdapter.LawyerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lawyer, parent, false)
        return LawyerViewHolder(view)
    }

    override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
        val lawyer = lawyers[position]
        holder.bind(lawyer)
        holder.itemView.setOnClickListener { onLawyerClick(lawyer) }
    }

    override fun getItemCount() = lawyers.size

    fun updateData(newLawyers: List<User>) {
        lawyers = newLawyers
        notifyDataSetChanged()
    }

    class LawyerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.lawyerName)
        private val specialtyText: TextView = itemView.findViewById(R.id.lawyerSpecialty)
        private val experienceText: TextView = itemView.findViewById(R.id.lawyerExperience)

        fun bind(lawyer: User) {
            nameText.text = lawyer.name
            specialtyText.text = lawyer.specialization
            experienceText.text = "${lawyer.experience} years"
        }
    }
}