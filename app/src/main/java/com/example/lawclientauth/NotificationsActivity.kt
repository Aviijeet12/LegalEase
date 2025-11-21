package com.example.lawclientauth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ActivityNotificationBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var adapter: NotificationAdapter

    private val notifications = mutableListOf<NotificationModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockNotifications()
        setupRecycler()
        setupMarkAllReadButton()
        setupSwipeGestures()
    }

    private fun loadMockNotifications() {
        val now = System.currentTimeMillis()

        notifications.add(
            NotificationModel(
                title = "New Document Uploaded",
                message = "Lawyer added new evidence for your case.",
                timestamp = now - 1000 * 60 * 60,
                type = "case"
            )
        )

        notifications.add(
            NotificationModel(
                title = "Appointment Reminder",
                message = "You have a meeting tomorrow at 11:00 AM.",
                timestamp = now - 1000 * 60 * 120,
                type = "appointment",
                isRead = true
            )
        )

        notifications.add(
            NotificationModel(
                title = "Payment Pending",
                message = "₹500 is pending for your last consultation.",
                timestamp = now - 1000 * 60 * 20,
                type = "payment"
            )
        )

        notifications.add(
            NotificationModel(
                title = "New Message",
                message = "Your lawyer sent you a message.",
                timestamp = now - 1000 * 60 * 5,
                type = "chat"
            )
        )
    }

    private fun setupRecycler() {
        adapter = NotificationAdapter(notifications)
        binding.recyclerNotifications.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotifications.adapter = adapter
    }

    private fun setupMarkAllReadButton() {
        binding.btnMarkAllRead.setOnClickListener {
            notifications.forEach { it.isRead = true }
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "All notifications marked as read", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSwipeGestures() {
        val swipe = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                notifications.removeAt(pos)
                adapter.notifyItemRemoved(pos)
            }
        }

        ItemTouchHelper(swipe).attachToRecyclerView(binding.recyclerNotifications)
    }
}
