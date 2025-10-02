package com.nyayasetu.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.databinding.ItemLanguageBinding

class LanguageAdapter(
    private val languages: List<Language>,
    private val onLanguageSelected: (Language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    private var selectedPosition = -1

    inner class ViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(language: Language, position: Int) {
            binding.languageCode.text = language.code
            binding.languageName.text = language.name

            // Set selection state
            binding.languageCard.isSelected = position == selectedPosition

            binding.languageCard.setOnClickListener {
                selectedPosition = position
                onLanguageSelected(language)
                notifyDataSetChanged() // Refresh all items to update selection
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(languages[position], position)
    }

    override fun getItemCount(): Int = languages.size
}