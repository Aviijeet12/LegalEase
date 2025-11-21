package com.example.lawclientauth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityLawyerAppointmentsBinding

class LawyerAppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerAppointmentsBinding
    private lateinit var adapter: LawyerAppointmentAdapter

    private val appointments = mutableListOf<AppointmentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockData()
        setupFilter()
        setupSearch()
        setupRecycler()
    }

    private fun loadMockData() {
        appointments.add(
            AppointmentModel(
                "Rahul Verma", "Property Dispute Discussion", 
                "12 Feb 2025", "04:30 PM", "Video Call", "Upcoming"
            )
        )
        appointments.add(
            AppointmentModel(
                "Aditi Sharma", "Contract Review",
                "10 Feb 2025", "11:00 AM", "In-Person", "Completed"
            )
        )
        appointments.add(
            AppointmentModel(
                "Sanjay Rao", "Bail Petition Review",
                "09 Feb 2025", "05:00 PM", "Audio Call", "Cancelled"
            )
        )
        appointments.add(
            AppointmentModel(
                "Nikhil Kumar", "Family Court Case",
                "14 Feb 2025", "03:15 PM", "Video Call", "Upcoming"
            )
        )
    }

    private fun setupFilter() {
        val filters = listOf("All", "Upcoming", "Completed", "Cancelled")
        val adapterFilter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filters)
        binding.dropdownFilter.setAdapter(adapterFilter)

        binding.dropdownFilter.setOnItemClickListener { _, _, _, _ ->
            filterAppointments(binding.dropdownFilter.text.toString())
        }
    }

    private fun filterAppointments(filter: String) {
        if (filter == "All") {
            adapter.updateList(appointments)
        } else {
            adapter.updateList(appointments.filter { it.status == filter })
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s.toString().trim()
                if (q.isEmpty()) {
                    adapter.updateList(appointments)
                } else {
                    adapter.updateList(
                        appointments.filter {
                            it.clientName.contains(q, true) ||
                                    it.purpose.contains(q, true)
                        }
                    )
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupRecycler() {
        adapter = LawyerAppointmentAdapter(
            appointments,
            onJoinCall = { appt ->
                // UI only
            },
            onMarkComplete = { appt ->
                appt.status = "Completed"
                adapter.notifyDataSetChanged()
            },
            onCancel = { appt ->
                appt.status = "Cancelled"
                adapter.notifyDataSetChanged()
            }
        )

        binding.recyclerAppointments.layoutManager = LinearLayoutManager(this)
        binding.recyclerAppointments.adapter = adapter
    }
}
