package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportBinding
    private lateinit var faqAdapter: FaqAdapter

    private val faqList = listOf(
        FaqModel(
            question = "How do I book an appointment?",
            answer = "Go to the Appointments tab > Select lawyer > Choose a time slot."
        ),
        FaqModel(
            question = "How do I upload legal documents?",
            answer = "Open your Case Details > Click 'Upload Document' button."
        ),
        FaqModel(
            question = "How do I contact my lawyer?",
            answer = "Use in-app chat or schedule a call from the Case Details section."
        ),
        FaqModel(
            question = "How does payment work?",
            answer = "You will receive a payment request after the lawyer sets the consultation fee."
        ),
        FaqModel(
            question = "Is my data secure?",
            answer = "Yes, all data is encrypted. We follow industry-standard security practices."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFaqRecycler()
        setupListeners()
    }

    private fun setupFaqRecycler() {
        faqAdapter = FaqAdapter(faqList)
        binding.recyclerFaq.layoutManager = LinearLayoutManager(this)
        binding.recyclerFaq.adapter = faqAdapter
    }

    private fun setupListeners() {

        binding.btnRaiseTicket.setOnClickListener {
            startActivity(Intent(this, SupportTicketActivity::class.java))
        }

        binding.btnEmailSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:legalhelp@legalease.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Need Help - Legal Ease")
            startActivity(intent)
        }

        binding.btnCallSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+1800123456")
            startActivity(intent)
        }

        binding.btnChatSupport.setOnClickListener {
            // UI only
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }
}
