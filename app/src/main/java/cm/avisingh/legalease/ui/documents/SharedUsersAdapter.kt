package cm.avisingh.legalease.ui.documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemSharedUserBinding

class SharedUsersAdapter(
    private val onRemoveClick: (String) -> Unit
) : ListAdapter<String, SharedUsersAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSharedUserBinding.inflate(
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
        private val binding: ItemSharedUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(email: String) {
            binding.apply {
                emailText.text = email
                permissionsText.text = "Can view and download"
                
                // Set first letter as avatar
                val initial = email.firstOrNull()?.uppercaseChar() ?: 'U'
                avatarImage.text = initial.toString()

                removeButton.setOnClickListener {
                    onRemoveClick(email)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }
}