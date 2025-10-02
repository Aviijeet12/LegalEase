package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R
import com.nyayasetu.app.models.User

class AdminUsersAdapter(
    private var users: List<User>,
    private val onUserClick: (User) -> Unit = {}
) : RecyclerView.Adapter<AdminUsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lawyer, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener { onUserClick(user) }
    }

    override fun getItemCount() = users.size

    fun updateData(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.lawyerName)
        private val emailText: TextView = itemView.findViewById(R.id.lawyerSpecialty)
        private val roleText: TextView = itemView.findViewById(R.id.lawyerExperience)

        fun bind(user: User) {
            nameText.text = user.name
            emailText.text = user.email
            roleText.text = user.role
        }
    }
}