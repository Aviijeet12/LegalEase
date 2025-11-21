package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityVoiceCallBinding

class VoiceCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVoiceCallBinding
    private var muted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHangup.setOnClickListener { finish() }
        binding.btnMute.setOnClickListener {
            muted = !muted
            binding.btnMute.alpha = if (muted) 0.6f else 1f
            // TODO: toggle audio stream
        }
    }
}
