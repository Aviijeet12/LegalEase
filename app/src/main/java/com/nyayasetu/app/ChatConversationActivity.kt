package com.nyayasetu.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.ActivityChatConversationBinding

class ChatConversationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatConversationBinding
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChat()
        setupMessageInput()
    }

    private fun setupChat() {
        val messages = listOf(
            Message("Hello, I need help with my employment contract.", "2024-01-20 10:30:00", false),
            Message("I'll review your contract and get back to you shortly.", "2024-01-20 10:32:00", true),
            Message("Thank you! I've uploaded the document.", "2024-01-20 10:35:00", false),
            Message("I can see the document. Let me analyze the clauses.", "2024-01-20 10:40:00", true)
        )

        adapter = MessageAdapter(messages)
        binding.messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.messagesRecyclerView.adapter = adapter
    }

    private fun setupMessageInput() {
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                adapter.addMessage(Message(message, "Just now", false))
                binding.messageInput.text.clear()
                binding.messagesRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }

        binding.attachButton.setOnClickListener {
            // Implement file attachment
        }
    }
}

data class Message(val text: String, val timestamp: String, val isSender: Boolean)