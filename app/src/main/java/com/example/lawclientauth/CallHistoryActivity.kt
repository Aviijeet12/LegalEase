package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityCallHistoryBinding

class CallHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerCalls.layoutManager = LinearLayoutManager(this)
        val sample = listOf(
            CallModel("c1","Adv. A Sharma", "video", System.currentTimeMillis() - 3600000, 600, "completed"),
            CallModel("c2","Adv. R Mehra", "voice", System.currentTimeMillis() - 7200000, 0, "missed")
        )
        binding.recyclerCalls.adapter = CallAdapter(sample) { call ->
            // Re-dial UI (frontend only)
            val intent = Intent(this, if (call.type=="video") VideoCallActivity::class.java else VoiceCallActivity::class.java)
            startActivity(intent)
        }
    }
}
