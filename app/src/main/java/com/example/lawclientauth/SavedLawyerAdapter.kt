package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemSavedLawyerBinding

class SavedLawyerAdapter(
    private val list: List<AiLawyerModel>,
    private val onViewProfile: (AiLawyerModel) -> Unit,
    private val onRemove: (AiLawyerModel) -> Unit
) : RecyclerView.Adapter<SavedLawyerAdapter.SavedVH>() {

    inner class SavedVH(val binding: ItemSavedLawyerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVH {
        val binding = ItemSavedLawyerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedVH(binding)
    }

    override fun onBindViewHolder(holder: SavedVH, position: Int) {
        val lawyer = list[position]

        holder.binding.tvName.text = lawyer.name
        holder.binding.tvSpecialization.text = lawyer.specialization
        holder.binding.tvRating.text = "⭐ ${lawyer.rating}"
        holder.binding.tvPrice.text = "₹${lawyer.price}"
        holder.binding.tvExperience.text = "${lawyer.experience} yrs exp"

        holder.binding.btnProfile.setOnClickListener {
            onViewProfile(lawyer)
        }

        holder.binding.btnRemove.setOnClickListener {
            onRemove(lawyer)
        }
    }

    override fun getItemCount(): Int = list.size
}
