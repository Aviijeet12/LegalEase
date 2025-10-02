package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentAdminUsersBinding

class AdminUsersFragment : Fragment() {

    private lateinit var binding: FragmentAdminUsersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserStats()
        setupUsersList()
    }

    private fun setupUserStats() {
        binding.totalClients.text = "1158"
        binding.totalLawyers.text = "89"
        binding.dailyActive.text = "456"
    }

    private fun setupUsersList() {
        val users = listOf(
            AdminUser(
                "John Doe",
                "john.doe@email.com",
                "Client",
                "3",
                "$5,600.00",
                "2 hours ago",
                "Joined 1/15/2023"
            ),
            AdminUser(
                "Sarah Johnson",
                "sarah.johnson@legal.com",
                "Lawyer",
                "47",
                "4.8",
                "1 hour ago",
                "Joined 6/10/2022"
            ),
            AdminUser(
                "Michael Chen",
                "michael.chen@legal.com",
                "Lawyer",
                "32",
                "4.7",
                "Active",
                "Joined 8/15/2022"
            ),
            AdminUser(
                "Jane Smith",
                "jane.smith@email.com",
                "Client",
                "1",
                "$3,200.00",
                "1 day ago",
                "Joined 3/20/2023"
            )
        )

        val adapter = AdminUsersAdapter(users)
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.usersRecyclerView.adapter = adapter
    }
}

data class AdminUser(
    val name: String,
    val email: String,
    val type: String,
    val cases: String,
    val value: String, // Spent for clients, Rating for lawyers
    val lastActive: String,
    val joined: String
)