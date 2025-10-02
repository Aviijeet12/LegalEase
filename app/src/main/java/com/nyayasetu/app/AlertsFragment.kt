package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentAlertsBinding

class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNotifications()
    }

    private fun setupNotifications() {
        val notifications = listOf(
            Notification(
                "Case Update (Important)",
                "Sarah Johnson has reviewed your employment contract...",
                "Sarah Johnson • Employment Lawyer",
                "13m ago",
                true
            ),
            Notification(
                "New Message",
                "You have received a new message from Michael Che...",
                "Michael Chen • Real Estate Lawyer",
                "23m ago",
                false
            ),
            Notification(
                "Payment Reminder (Important)",
                "Your invoice #LG-2024-001 for $2,500 is due in 2 days",
                null,
                "38m ago",
                true
            ),
            Notification(
                "Document Uploaded",
                "Your employment contract has been uploaded",
                null,
                "1h ago",
                false
            )
        )

        val adapter = NotificationsAdapter(notifications)
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationsRecyclerView.adapter = adapter

        binding.markAllRead.setOnClickListener {
            // Mark all as read logic
        }
    }
}

data class Notification(
    val title: String,
    val message: String,
    val sender: String?,
    val time: String,
    val important: Boolean
)