package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R
import com.nyayasetu.app.models.User

class ClientsAdapter(
    private var clients: List<User>,
    private val onClientClick: (User) -> Unit = {}
) : RecyclerView.Adapter<ClientsAdapter.ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lawyer, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clients[position]
        holder.bind(client)
        holder.itemView.setOnClickListener { onClientClick(client) }
    }

    override fun getItemCount() = clients.size

    fun updateData(newClients: List<User>) {
        clients = newClients
        notifyDataSetChanged()
    }

    class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.lawyerName)
        private val emailText: TextView = itemView.findViewById(R.id.lawyerSpecialty)
        private val phoneText: TextView = itemView.findViewById(R.id.lawyerExperience)

        fun bind(client: User) {
            nameText.text = client.name
            emailText.text = client.email
            phoneText.text = client.phone
        }
    }
}