package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityContactListBinding

class ContactListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactListBinding
    private lateinit var adapter: ContactAdapter
    private val contacts = mutableListOf<ContactModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockContacts()
        setupRecycler()
    }

    private fun loadMockContacts() {
        contacts.add(
            ContactModel(
                id = "law1",
                name = "Adv. Ananya Sharma",
                role = "lawyer",
                lastMessage = "Please check the updated draft.",
                isOnline = true
            )
        )

        contacts.add(
            ContactModel(
                id = "cl1",
                name = "Client: Rahul Verma",
                role = "client",
                lastMessage = "Thank you for your guidance!",
                isOnline = false
            )
        )

        contacts.add(
            ContactModel(
                id = "law2",
                name = "Adv. M. Khurana",
                role = "lawyer",
                lastMessage = "Let's schedule a meeting.",
                isOnline = true
            )
        )

        contacts.add(
            ContactModel(
                id = "cl2",
                name = "Client: Sneha Kapoor",
                role = "client",
                lastMessage = "Uploaded the court notice.",
                isOnline = false
            )
        )
    }

    private fun setupRecycler() {
        adapter = ContactAdapter(contacts,
            onChat = { contact ->
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("otherName", contact.name)
                intent.putExtra("otherId", contact.id)
                startActivity(intent)
            },
            onCall = { contact ->
                // UI-only
                android.widget.Toast.makeText(this, "Calling ${contact.name}", android.widget.Toast.LENGTH_SHORT).show()
            },
            onVideoCall = { contact ->
                android.widget.Toast.makeText(this, "Video calling ${contact.name}", android.widget.Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerContacts.adapter = adapter
    }
}
