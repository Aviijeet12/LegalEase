package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemFaqBinding
import cm.avisingh.legalease.models.FAQ

class FAQAdapter(
    private val faqList: List<FAQ>,
    private val onFaqClick: (FAQ, Int) -> Unit
) : RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val binding = ItemFaqBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FAQViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        holder.bind(faqList[position], position)
    }

    override fun getItemCount() = faqList.size

    inner class FAQViewHolder(
        private val binding: ItemFaqBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(faq: FAQ, position: Int) {
            binding.questionText.text = faq.question
            binding.answerText.text = faq.answer
            
            // Show/hide answer based on expanded state
            binding.answerText.visibility = if (faq.isExpanded) View.VISIBLE else View.GONE
            binding.expandIcon.rotation = if (faq.isExpanded) 180f else 0f
            
            binding.questionLayout.setOnClickListener {
                onFaqClick(faq, position)
            }
        }
    }
}
