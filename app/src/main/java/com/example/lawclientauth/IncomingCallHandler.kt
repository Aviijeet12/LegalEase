package com.example.lawclientauth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.example.lawclientauth.databinding.DialogIncomingCallBinding

/**
 * Small helper to show incoming call dialog.
 * When user accepts -> opens VideoCallActivity (or VoiceCallActivity)
 * When user declines -> dismiss
 *
 * TODO: integrate with actual signalling code to trigger this when a call arrives.
 */
object IncomingCallHandler {

    fun showIncomingCall(context: Context, callerName: String, isVideo: Boolean = true) {
        val binding = DialogIncomingCallBinding.inflate(LayoutInflater.from(context))
        binding.tvCallerName.text = callerName
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.btnAccept.setOnClickListener {
            dialog.dismiss()
            val cls = if (isVideo) VideoCallActivity::class.java else VoiceCallActivity::class.java
            context.startActivity(Intent(context, cls))
        }

        binding.btnDecline.setOnClickListener {
            dialog.dismiss()
            // TODO: send decline signaling message
        }

        dialog.show()
    }
}
