package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentClientManagementBinding

class ClientManagementFragment : Fragment() {

    private lateinit var binding: FragmentClientManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClientManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClients()
    }

    private fun setupClients() {
        val clients = listOf(
            Client(
                "JD",
                "John Doe",
                "john.doe@email.com",
                "+1 (555) 123-4567",
                "Active",
                "2",
                "$5,600.00",
                "2 hours ago",
                "Member since 1/15/2023"
            ),
            Client(
                "JS",
                "Jane Smith",
                "jane.smith@email.com",
                "+1 (555) 234-5678",
                "Active",
                "1",
                "$3,200.00",
                "1 day ago",
                "Member since 3/20/2023"
            ),
            Client(
                "MJ",
                "Mike Johnson",
                "mike.johnson@email.com",
                "+1 (555) 345-6789",
                "Past client",
                "0",
                "$0.00",
                "2 weeks ago",
                "Former client"
            )
        )

        val adapter = ClientsAdapter(clients)
        binding.clientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.clientsRecyclerView.adapter = adapter
    }
}

data class Client(
    val initials: String,
    val name: String,
    val email: String,
    val phone: String,
    val status: String,
    val totalCases: String,
    val totalPaid: String,
    val lastContact: String,
    val memberSince: String
)