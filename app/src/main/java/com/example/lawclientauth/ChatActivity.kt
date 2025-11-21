package com.example.lawclientauth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityChatBinding

/**
 * Universal ChatActivity used by both client & lawyer.
 *
 * Intent extras accepted:
 *  - "otherName": String (display name)
 *  - "otherId": String (id used to identify other user)
 *
 * Frontend-only: typing indicator and read receipts are simulated here.
 */
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<MessageModel>()
    private val handler = Handler(Looper.getMainLooper())

    // Mock file path provided by developer (uploaded zip)
    private val mockFilePath = "/mnt/data/le-z.zip"

    // Simulated other user id
    private var otherId: String = "other"
    private var otherName: String = "Lawyer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otherName = intent.getStringExtra("otherName") ?: "Lawyer"
        otherId = intent.getStringExtra("otherId") ?: "other"

        binding.tvChatTitle.text = otherName

        setupRecycler()
        setupInput()
        loadMockConversation()
        setupHeaderButtons()
    }

    private fun setupHeaderButtons() {
        binding.btnCall.setOnClickListener {
            Toast.makeText(this, "Start audio call (UI only)", Toast.LENGTH_SHORT).show()
        }
        binding.btnVideoCall.setOnClickListener {
            Toast.makeText(this, "Start video call (UI only)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecycler() {
        adapter = ChatAdapter(messages,
            onOpenFile = { url -> openFile(url) },
            onOpenImage = { url -> openImagePreview(url) }
        )
        binding.recyclerChat.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        binding.recyclerChat.adapter = adapter
    }

    private fun setupInput() {
        binding.btnSend.setOnClickListener { sendTextMessage() }
        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) { sendTextMessage(); true } else false
        }

        binding.btnAttach.setOnClickListener { attachFile() }
        binding.btnImage.setOnClickListener { attachImage() }
    }

    private fun sendTextMessage() {
        val text = binding.etMessage.text.toString().trim()
        if (text.isEmpty()) return

        val msg = MessageModel(
            senderId = "me",
            text = text,
            type = "text",
            status = "sending"
        )
        pushOutgoingMessage(msg)
    }

    private fun attachFile() {
        // UI-only: send a file message pointing to mock file path
        val msg = MessageModel(
            senderId = "me",
            text = "Document.pdf",
            type = "file",
            fileUrl = mockFilePath,
            status = "sent"
        )
        pushOutgoingMessage(msg)
        Toast.makeText(this, "Attached file (mock).", Toast.LENGTH_SHORT).show()
    }

    private fun attachImage() {
        // UI-only: use same mock path as "image" (in real app open image picker and upload)
        val msg = MessageModel(
            senderId = "me",
            text = "Image.jpg",
            type = "image",
            fileUrl = mockFilePath,
            status = "sent"
        )
        pushOutgoingMessage(msg)
        Toast.makeText(this, "Attached image (mock).", Toast.LENGTH_SHORT).show()
    }

    private fun pushOutgoingMessage(msg: MessageModel) {
        messages.add(msg)
        adapter.notifyItemInserted(messages.size - 1)
        binding.recyclerChat.scrollToPosition(messages.size - 1)
        binding.etMessage.text?.clear()

        // Simulate status transitions: sending -> sent -> delivered -> read
        handler.postDelayed({
            msg.status = "sent"
            adapter.notifyItemChanged(messages.indexOf(msg))
        }, 300)
        handler.postDelayed({
            msg.status = "delivered"
            adapter.notifyItemChanged(messages.indexOf(msg))
            simulateReplyAndRead(msg)
        }, 800)
    }

    private fun simulateReplyAndRead(original: MessageModel) {
        // Show 'typing' indicator
        binding.tvTypingIndicator.apply {
            text = "$otherName is typing..."
            alpha = 1f
            visibility = android.view.View.VISIBLE
        }

        handler.postDelayed({
            binding.tvTypingIndicator.visibility = android.view.View.GONE
            // mock reply from other user
            val reply = MessageModel(
                senderId = otherId,
                text = "Thanks, I will review this.",
                type = "text",
                status = "sent"
            )
            messages.add(reply)
            adapter.notifyItemInserted(messages.size - 1)
            binding.recyclerChat.scrollToPosition(messages.size - 1)

            // mark original as read
            handler.postDelayed({
                original.status = "read"
                adapter.notifyItemChanged(messages.indexOf(original))
                // mark reply delivered/read after tiny delay
                reply.status = "delivered"
                adapter.notifyItemChanged(messages.indexOf(reply))
                handler.postDelayed({
                    reply.status = "read"
                    adapter.notifyItemChanged(messages.indexOf(reply))
                }, 700)
            }, 900)
        }, 1200)
    }

    private fun loadMockConversation() {
        // starter messages with mix of types
        messages.clear()
        messages.add(MessageModel(senderId = otherId, text = "Hello, I received your documents.", type = "text", status = "read", timestamp = System.currentTimeMillis() - 1000L*60*60))
        messages.add(MessageModel(senderId = "me", text = "Thanks — waiting for your feedback.", type = "text", status = "read", timestamp = System.currentTimeMillis() - 1000L*60*55))
        messages.add(MessageModel(senderId = otherId, text = "Court Notice attached.", type = "file", fileUrl = mockFilePath, status = "read", timestamp = System.currentTimeMillis() - 1000L*60*20))
        messages.add(MessageModel(senderId = "me", text = "Uploaded additional evidence.", type = "file", fileUrl = mockFilePath, status = "read", timestamp = System.currentTimeMillis() - 1000L*60*10))
        messages.add(MessageModel(senderId = otherId, text = "See attached image.", type = "image", fileUrl = mockFilePath, status = "read", timestamp = System.currentTimeMillis() - 1000L*60*5))

        adapter.notifyDataSetChanged()
        binding.recyclerChat.scrollToPosition(messages.size - 1)
    }

    private fun openFile(url: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.setDataAndType(Uri.parse(url), "*/*")
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open file (mock path).", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePreview(url: String) {
        // Simple intent to view image (mock)
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.setDataAndType(Uri.parse(url), "image/*")
            startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open image (mock path).", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
