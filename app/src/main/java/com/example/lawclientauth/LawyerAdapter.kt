package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemLawyerBinding

class LawyerAdapter(private val list: List<LawyerModel>) :
    RecyclerView.Adapter<LawyerAdapter.LawyerViewHolder>() {

    inner class LawyerViewHolder(val binding: ItemLawyerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
        val binding = ItemLawyerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LawyerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvName.text = item.name
        holder.binding.tvSpecialization.text = item.specialization
        holder.binding.tvRating.text = "⭐ ${item.rating}"
        holder.binding.tvPrice.text = "₹${item.price}"package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemLawyerBinding

class LawyerAdapter(private val list: List<LawyerModel>) :
    RecyclerView.Adapter<LawyerAdapter.LawyerViewHolder>() {

    inner class LawyerViewHolder(val binding: ItemLawyerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
        val binding = ItemLawyerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LawyerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvName.text = item.name
        holder.binding.tvSpecialization.text = item.specialization
        holder.binding.tvRating.text = "⭐ ${item.rating}"
        holder.binding.tvPrice.text = "₹${item.price}"
        holder.binding.tvExperience.text = "${item.experience} yrs experience"

        holder.binding.btnViewProfile.setOnClickListener {
            // Later → navigate to lawyer profile screen
        }
    }

    override fun getItemCount() = list.size
}

        holder.binding.tvExperience.text = "${item.experience} yrs experience"

        holder.binding.btnViewProfile.setOnClickListener {
            // Later → navigate to lawyer profile screen
        }
    }

    override fun getItemCount() = list.size
}
