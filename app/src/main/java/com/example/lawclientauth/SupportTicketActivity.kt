package com.example.lawclientauth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivitySupportTicketBinding

class SupportTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitTicket.setOnClickListener {
            val subject = binding.etSubject.text.toString().trim()
            val message = binding.etMessage.text.toString().trim()

            if (subject.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Ticket Submitted (UI only)", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
