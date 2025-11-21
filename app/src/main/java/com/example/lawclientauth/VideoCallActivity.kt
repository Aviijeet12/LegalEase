package com.example.lawclientauth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityVideoCallBinding

/**
 * Video call UI (frontend only).
 * Placeholder surfaces for local & remote video.
 * TODO: hook to WebRTC video streams.
 */
class VideoCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoCallBinding
    private var isMuted = false
    private var isCameraOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHangup.setOnClickListener { finish() } // end call (frontend)
        binding.btnMute.setOnClickListener {
            isMuted = !isMuted
            binding.btnMute.alpha = if (isMuted) 0.6f else 1f
            // TODO: toggle audio track
        }
        binding.btnSwitchCam.setOnClickListener {
            isCameraOn = !isCameraOn
            binding.btnSwitchCam.alpha = if (isCameraOn) 1f else 0.6f
            // TODO: switch camera
        }
    }
}
