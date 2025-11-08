package cm.avisingh.legalease.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemFaqBinding

class FAQAdapter(
    private val onFaqClick: (FAQ) -> Unit
) : ListAdapter<FAQ, FAQAdapter.ViewHolder>(FAQDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFaqBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemFaqBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFaqClick(getItem(position))
                }
            }
        }

        fun bind(faq: FAQ) {
            // TODO: Bind FAQ data to views when layout is available
        }
    }
}

private class FAQDiffCallback : DiffUtil.ItemCallback<FAQ>() {
    override fun areItemsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        return oldItem == newItem
    }
}
