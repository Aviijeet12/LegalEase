package cm.avisingh.legalease.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemSearchResultBinding

class SearchResultAdapter(
    private val onResultClick: (SearchResult) -> Unit
) : ListAdapter<SearchResult, SearchResultAdapter.ViewHolder>(SearchResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // Save the imageView reference for shared element transitions
        item.imageView = holder.binding.resultIcon
        holder.bind(item, onResultClick)
    }

    class ViewHolder(
        val binding: ItemSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: SearchResult, onResultClick: (SearchResult) -> Unit) {
            binding.apply {
                resultIcon.setImageResource(result.iconResId)
                categoryChip.text = result.category
                titleText.text = result.title
                descriptionText.text = result.description
                matchText.text = result.matchText

                // Set transition name for shared element transitions
                resultIcon.transitionName = "search_result_${result.id}"

                // Handle click with ripple effect
                root.setOnClickListener { onResultClick(result) }
            }
        }
    }
}

private class SearchResultDiffCallback : DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem.copy(imageView = oldItem.imageView)
    }
}