package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConversations()
    }

    private fun setupConversations() {
        val conversations = listOf(
            Conversation(
                "Sarah Johnson",
                "10 min ago",
                "Employment Lawyer",
                "I'll review your contract and g...",
                "Employment Law"
            ),
            Conversation(
                "Legal Support",
                "2 hours ago",
                "Customer Service",
                "How can we help you today?",
                null
            )
        )

        val adapter = ConversationsAdapter(conversations)
        binding.conversationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.conversationsRecyclerView.adapter = adapter
    }
}

data class Conversation(
    val name: String,
    val time: String,
    val role: String,
    val lastMessage: String,
    val category: String?
)