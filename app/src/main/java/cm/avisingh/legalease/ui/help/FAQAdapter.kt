package cm.avisingh.legalease.ui.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ItemFaqBinding

class FAQAdapter : ListAdapter<FAQ, FAQAdapter.ViewHolder>(FAQDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFaqBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemFaqBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.questionLayout.setOnClickListener {
                binding.answerText.isVisible = !binding.answerText.isVisible
                binding.expandIcon.setImageResource(
                    if (binding.answerText.isVisible) R.drawable.ic_expand_less
                    else R.drawable.ic_expand_more
                )
            }
        }

        fun bind(faq: FAQ) {
            binding.questionText.text = faq.question
            binding.answerText.text = faq.answer
        }
    }
}

private class FAQDiffCallback : DiffUtil.ItemCallback<FAQ>() {
    override fun areItemsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        return oldItem.question == newItem.question
    }

    override fun areContentsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        return oldItem == newItem
    }
}