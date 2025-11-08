package cm.avisingh.legalease.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.LegalEaseApplication
import cm.avisingh.legalease.adapters.ChatAdapter
import cm.avisingh.legalease.databinding.ActivityChatBinding
import cm.avisingh.legalease.models.Message
import cm.avisingh.legalease.utils.SharedPrefManager
import java.util.*
import cm.avisingh.legalease.utils.AnalyticsHelper

class ChatActivity : AppCompatActivity() {

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var binding: ActivityChatBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    // Mock chat partner data
    private val chatPartnerName = "Advocate Rajesh Sharma"
    private val chatPartnerId = "lawyer_rajesh@legalease.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analyticsHelper.trackScreen(this::class.java.simpleName)

        analyticsHelper = LegalEaseApplication.analyticsHelper
        analyticsHelper.trackScreenView("Chat Screen")
        analyticsHelper.logChatSessionStarted("convo_123", "client")
        sharedPrefManager = SharedPrefManager(this)
        setupChat()
        setupClickListeners()
        loadMockMessages()
    }

    private fun setupChat() {
        // Setup toolbar
        binding.tvChatUserName.text = chatPartnerName
        binding.tvChatStatus.text = "Online"

        // Setup RecyclerView
        chatAdapter = ChatAdapter(messages, sharedPrefManager)
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        // Scroll to bottom when new message arrives
        chatAdapter.registerAdapterDataObserver(object : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvMessages.scrollToPosition(chatAdapter.itemCount - 1)
            }
        })
    }

    private fun setupClickListeners() {
        // Back button
        binding.ivBack.setOnClickListener {
            finish()
        }

        // Send message
        binding.ivSend.setOnClickListener {
            sendMessage()
        }

        // Send on enter key
        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }

        // Attach file
        binding.ivAttach.setOnClickListener {
            showAttachmentOptions()
        }

        // Call buttons (mock for now)
        binding.ivCall.setOnClickListener {
            Toast.makeText(this, "Voice call feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.ivVideoCall.setOnClickListener {
            Toast.makeText(this, "Video call feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString().trim()
        analyticsHelper.trackEvent("message_sent", mapOf(
            "message_length" to messageText.length.toString()
        ))


        if (messageText.isEmpty()) {
            return
        }
        analyticsHelper.logChatMessageSent("convo_123", messageText.length)
        // Create new message
        val currentUserEmail = sharedPrefManager.getUserEmail()
        val currentUserName = sharedPrefManager.getUserName()

        val newMessage = Message(
            id = UUID.randomUUID().toString(),
            senderId = currentUserEmail,
            senderName = if (currentUserName.isNotEmpty()) currentUserName else "You",
            receiverId = chatPartnerId,
            message = messageText,
            timestamp = Date(),
            messageType = "text"
        )

        // Add to adapter
        chatAdapter.addMessage(newMessage)

        // Clear input
        binding.etMessage.text?.clear()

        // Simulate reply after 1-3 seconds
        simulateReply(messageText)
    }

    private fun simulateReply(userMessage: String) {
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val replies = arrayOf(
                "I'll review the documents and get back to you by tomorrow.",
                "That's a valid point. Let me check the legal precedents for this case.",
                "Please upload the relevant documents in the portal for my review.",
                "The court hearing has been scheduled for next Monday at 10 AM.",
                "I recommend we proceed with the settlement offer.",
                "I've filed the motion with the court. We should hear back soon.",
                "Can you provide more details about this issue?",
                "I'll prepare the legal brief and share it with you for review."
            )

            val randomReply = replies.random()

            val replyMessage = Message(
                id = UUID.randomUUID().toString(),
                senderId = chatPartnerId,
                senderName = chatPartnerName,
                receiverId = sharedPrefManager.getUserEmail(),
                message = randomReply,
                timestamp = Date(),
                messageType = "text"
            )

            chatAdapter.addMessage(replyMessage)
        }, (1000..3000).random().toLong())
    }

    private fun showAttachmentOptions() {
        val options = arrayOf("Document", "Image", "Cancel")

        android.app.AlertDialog.Builder(this)
            .setTitle("Attach File")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        Toast.makeText(this, "Document attachment coming soon!", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        Toast.makeText(this, "Image attachment coming soon!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }

    private fun loadMockMessages() {
        val welcomeMessage = Message(
            id = "1",
            senderId = chatPartnerId,
            senderName = chatPartnerName,
            receiverId = sharedPrefManager.getUserEmail(),
            message = "Hello! Welcome to LegalEase. I'm here to assist you with your legal matters. How can I help you today?",
            timestamp = Date(System.currentTimeMillis() - 3600000), // 1 hour ago
            messageType = "text"
        )

        messages.add(welcomeMessage)
        chatAdapter.notifyDataSetChanged()

        // Scroll to bottom
        binding.rvMessages.scrollToPosition(messages.size - 1)
    }
}