package cm.avisingh.legalease.ui.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemLegalGuideBinding

class LegalGuideAdapter(
    private val onGuideClick: (LegalGuide) -> Unit
) : ListAdapter<LegalGuide, LegalGuideAdapter.ViewHolder>(GuideDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLegalGuideBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onGuideClick)
    }

    class ViewHolder(
        private val binding: ItemLegalGuideBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(guide: LegalGuide, onGuideClick: (LegalGuide) -> Unit) {
            binding.apply {
                categoryText.text = guide.category
                titleText.text = guide.title
                descriptionText.text = guide.description
                guideImage.setImageResource(guide.imageResId)
                
                root.setOnClickListener { onGuideClick(guide) }
                readMoreButton.setOnClickListener { onGuideClick(guide) }
            }
        }
    }
}

private class GuideDiffCallback : DiffUtil.ItemCallback<LegalGuide>() {
    override fun areItemsTheSame(oldItem: LegalGuide, newItem: LegalGuide): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: LegalGuide, newItem: LegalGuide): Boolean {
        return oldItem == newItem
    }
}