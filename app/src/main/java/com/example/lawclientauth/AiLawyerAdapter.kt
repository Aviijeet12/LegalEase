package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemAiLawyerBinding

class AiLawyerAdapter(
    private val list: List<AiLawyerModel>,
    private val onClick: (AiLawyerModel) -> Unit
) : RecyclerView.Adapter<AiLawyerAdapter.AiLawyerVH>() {

    inner class AiLawyerVH(val binding: ItemAiLawyerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiLawyerVH {
        val binding = ItemAiLawyerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AiLawyerVH(binding)
    }

    override fun onBindViewHolder(holder: AiLawyerVH, position: Int) {
        val lawyer = list[position]

        holder.binding.tvName.text = lawyer.name
        holder.binding.tvSpecialization.text = lawyer.specialization
        holder.binding.tvRating.text = "⭐ ${lawyer.rating}"
        holder.binding.tvPrice.text = "₹${lawyer.price}"
        holder.binding.tvExperience.text = "${lawyer.experience} yrs exp"
        holder.binding.tvMatchScore.text = "${lawyer.matchScore}% match"

        holder.binding.btnViewProfile.setOnClickListener {
            onClick(lawyer)
        }
    }

    override fun getItemCount(): Int = list.size
}
