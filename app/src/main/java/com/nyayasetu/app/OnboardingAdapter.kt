package com.nyayasetu.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.databinding.ItemOnboardingBinding

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val onButtonClick: (Int) -> Unit
) : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnboardingItem, position: Int) {
            binding.titleText.text = item.title
            binding.descriptionText.text = item.description
            binding.imageText.text = item.imageText
            binding.nextButton.text = item.buttonText

            binding.nextButton.setOnClickListener {
                onButtonClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}