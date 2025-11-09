package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ItemClientBinding
import cm.avisingh.legalease.models.Client

class ClientsAdapter(
    private val clients: List<Client>,
    private val onClientClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(client: Client) {
            binding.apply {
                // Set client initials in avatar
                val initials = client.name.split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString("")
                tvClientInitial.text = initials

                tvClientName.text = client.name
                tvClientEmail.text = client.email
                tvClientPhone.text = client.phone
                tvActiveCases.text = "${client.activeCases} Active Cases"
                tvTotalCases.text = "Total: ${client.totalCases} cases"

                // Set status badge
                tvClientStatus.text = client.status
                when (client.status) {
                    "Active" -> {
                        tvClientStatus.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.status_active)
                        )
                    }
                    else -> {
                        tvClientStatus.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.status_inactive)
                        )
                    }
                }

                root.setOnClickListener { onClientClick(client) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clients[position])
    }

    override fun getItemCount() = clients.size
}
