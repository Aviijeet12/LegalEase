package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityCallLogBinding
import java.util.concurrent.TimeUnit

class CallLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallLogBinding
    private lateinit var adapter: CallLogAdapter
    private val callLogs = mutableListOf<CallLogModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockData()
        setupRecycler()
    }

    private fun loadMockData() {
        // Simulated call data
        callLogs.add(
            CallLogModel(
                name = "Adv. Ananya Sharma",
                time = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(12),
                duration = 180,
                type = "audio",
                status = "incoming"
            )
        )
        callLogs.add(
            CallLogModel(
                name = "Client: Rohan Mehta",
                time = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1),
                duration = 0,
                type = "video",
                status = "missed"
            )
        )
        callLogs.add(
            CallLogModel(
                name = "Adv. Rahul Khanna",
                time = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1),
                duration = 320,
                type = "audio",
                status = "outgoing"
            )
        )
    }

    private fun setupRecycler() {
        adapter = CallLogAdapter(callLogs)
        binding.recyclerCallLogs.layoutManager = LinearLayoutManager(this)
        binding.recyclerCallLogs.adapter = adapter
    }
}
