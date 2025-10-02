package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentSystemLogsBinding

class SystemLogsFragment : Fragment() {

    private lateinit var binding: FragmentSystemLogsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSystemLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSystemLogs()
    }

    private fun setupSystemLogs() {
        val logs = listOf(
            SystemLog(
                "INFO",
                "System backup completed",
                "2024-01-20 03:00:00",
                "Backup"
            ),
            SystemLog(
                "WARNING",
                "High memory usage detected",
                "2024-01-20 02:30:00",
                "Performance"
            ),
            SystemLog(
                "INFO",
                "New user registration",
                "2024-01-20 02:15:00",
                "User Management"
            ),
            SystemLog(
                "ERROR",
                "Database connection timeout",
                "2024-01-20 01:45:00",
                "Database"
            )
        )

        val adapter = SystemLogsAdapter(logs)
        binding.logsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.logsRecyclerView.adapter = adapter
    }
}

data class SystemLog(
    val level: String,
    val message: String,
    val timestamp: String,
    val category: String
)