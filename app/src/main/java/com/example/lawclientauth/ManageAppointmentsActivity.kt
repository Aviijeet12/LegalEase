package com.example.lawclientauth

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityManageAppointmentsBinding
import java.util.*

class ManageAppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageAppointmentsBinding
    private lateinit var adapter: AppointmentActionAdapter

    private val allAppointments = mutableListOf<AppointmentModel>()
    private val mockUrl = "/mnt/data/le-z.zip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockData()
        setupFilterDropdown()
        setupRecycler()
    }

    private fun loadMockData() {
        allAppointments.add(
            AppointmentModel(
                title = "Property Consultation",
                time = "11:00 AM, 18 Jan 2025",
                lawyerName = "Adv. A Sharma",
                status = "Upcoming",
                documentUrl = mockUrl
            )
        )

        allAppointments.add(
            AppointmentModel(
                title = "Corporate Case Review",
                time = "03:30 PM, 12 Jan 2025",
                lawyerName = "Adv. S Kapoor",
                status = "Completed",
                documentUrl = mockUrl
            )
        )

        allAppointments.add(
            AppointmentModel(
                title = "Hearing Prep",
                time = "10:00 AM, 10 Jan 2025",
                lawyerName = "Adv. J Rao",
                status = "Cancelled",
                documentUrl = mockUrl
            )
        )
    }

    private fun setupFilterDropdown() {
        val filters = listOf("All", "Upcoming", "Completed", "Cancelled")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filters)
        binding.dropdownFilter.setAdapter(adapter)

        binding.dropdownFilter.setOnItemClickListener { _, _, _, _ ->
            applyFilter(binding.dropdownFilter.text.toString())
        }
    }

    private fun setupRecycler() {
        adapter = AppointmentActionAdapter(allAppointments)
        binding.recyclerManageAppointments.layoutManager = LinearLayoutManager(this)
        binding.recyclerManageAppointments.adapter = adapter
    }

    private fun applyFilter(filter: String) {
        if (filter == "All") {
            adapter.updateList(allAppointments)
            return
        }

        val filtered = allAppointments.filter { it.status == filter }
        adapter.updateList(filtered)
    }
}
