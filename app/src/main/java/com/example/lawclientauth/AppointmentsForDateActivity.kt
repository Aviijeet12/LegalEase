package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityAppointmentsForDateBinding

class AppointmentsForDateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentsForDateBinding
    private val sampleDocUrl = "/mnt/data/le-z.zip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentsForDateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = intent.getStringExtra("dateKey") ?: ""
        binding.tvDateHeader.text = "Appointments on $date"

        // For now: show mock single item or pulled from a shared source later
        val list = listOf(
            AppointmentRequestModel("reqX", "Rahul Verma", date, "11:00 AM", "Video Call", "Discussion", listOf(sampleDocUrl), "Accepted")
        )

        val adapter = AppointmentRequestAdapter(list,
            onAccept = {},
            onReject = {},
            onViewClient = {}
        )
        binding.recyclerForDate.layoutManager = LinearLayoutManager(this)
        binding.recyclerForDate.adapter = adapter
    }
}
