package com.example.lawclientauth

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityBookAppointmentBinding
import java.util.*

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookAppointmentBinding
    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLawyerDropdown()
        setupClicks()
    }

    private fun setupLawyerDropdown() {
        val lawyers = listOf(
            "Adv. A Sharma",
            "Adv. R Mehra",
            "Adv. S Kapoor",
            "Adv. J Rao"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lawyers)
        binding.dropdownLawyer.setAdapter(adapter)
    }

    private fun setupClicks() {
        binding.btnSelectDate.setOnClickListener { openDatePicker() }
        binding.btnSelectTime.setOnClickListener { openTimePicker() }
        binding.btnConfirmAppointment.setOnClickListener { confirmAppointment() }
    }

    private fun openDatePicker() {
        val c = Calendar.getInstance()
        val dp = DatePickerDialog(
            this,
            { _, y, m, d ->
                selectedDate = "$d-${m+1}-$y"
                binding.tvSelectedDate.text = selectedDate
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )
        dp.show()
    }

    private fun openTimePicker() {
        val c = Calendar.getInstance()
        val tp = TimePickerDialog(
            this,
            { _, h, min ->
                selectedTime = String.format("%02d:%02d", h, min)
                binding.tvSelectedTime.text = selectedTime
            },
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            true
        )
        tp.show()
    }

    private fun confirmAppointment() {
        val lawyer = binding.dropdownLawyer.text.toString().trim()
        val reason = binding.etReason.text.toString().trim()

        if (lawyer.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Appointment booked (Mock)", Toast.LENGTH_LONG).show()

        // TODO: Send this to backend (later)
        finish()
    }
}
