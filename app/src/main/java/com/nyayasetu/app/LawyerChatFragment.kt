package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentLawyerChatBinding

class LawyerChatFragment : Fragment() {

    private lateinit var binding: FragmentLawyerChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLawyerChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConversations()
        setupFilterTabs()
    }

    private fun setupFilterTabs() {
        binding.tabAll.setOnClickListener { updateChatTabSelection(0) }
        binding.tabUnread.setOnClickListener { updateChatTabSelection(1) }
        binding.tabPriority.setOnClickListener { updateChatTabSelection(2) }
    }

    private fun updateChatTabSelection(selectedTab: Int) {
        val tabs = listOf(binding.tabAll, binding.tabUnread, binding.tabPriority)
        tabs.forEachIndexed { index, tab ->
            tab.isSelected = index == selectedTab
            tab.setBackgroundColor(
                if (index == selectedTab) getColor(android.R.color.holo_blue_light)
                else getColor(android.R.color.transparent)
            )
        }
    }

    private fun setupConversations() {
        val conversations = listOf(
            LawyerConversation(
                "John Doe",
                "2 min ago",
                "Employment Case Client",
                "Thank you for the update...",
                "Employment Law",
                2
            ),
            LawyerConversation(
                "Sarah Wilson",
                "15 min ago",
                "Property Dispute Client",
                "I have the documents you...",
                "Property Law",
                1
            ),
            LawyerConversation(
                "Michael Chen",
                "1 hour ago",
                "Contract Review Client",
                "When can we schedule the...",
                "Contract Law",
                0
            )
        )

        val adapter = LawyerConversationsAdapter(conversations)
        binding.conversationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.conversationsRecyclerView.adapter = adapter
    }
}

data class LawyerConversation(
    val name: String,
    val time: String,
    val role: String,
    val lastMessage: String,
    val category: String,
    val unreadCount: Int
)