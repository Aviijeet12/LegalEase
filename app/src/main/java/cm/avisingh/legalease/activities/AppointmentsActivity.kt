package cm.avisingh.legalease.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.adapters.AppointmentsAdapter
import cm.avisingh.legalease.databinding.ActivityAppointmentsBinding
import cm.avisingh.legalease.models.Appointment
import cm.avisingh.legalease.utils.SharedPrefManager

class AppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentsBinding
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var sharedPrefManager: SharedPrefManager
    private val appointmentsList = mutableListOf<Appointment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager(this)

        setupToolbar()
        setupRecyclerView()
        loadAppointments()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Appointments"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        appointmentsAdapter = AppointmentsAdapter(appointmentsList) { appointment ->
            showAppointmentDetails(appointment)
        }
        binding.rvAppointments.apply {
            layoutManager = LinearLayoutManager(this@AppointmentsActivity)
            adapter = appointmentsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabNewAppointment.setOnClickListener {
            showNewAppointmentDialog()
        }
    }

    private fun loadAppointments() {
        // Demo appointments
        appointmentsList.clear()
        appointmentsList.addAll(listOf(
            Appointment(
                id = "1",
                title = "Initial Consultation",
                description = "Discuss case details and strategy",
                lawyerName = "Adv. Rajesh Kumar",
                dateTime = System.currentTimeMillis() + (3L * 24 * 60 * 60 * 1000), // 3 days from now
                duration = 60,
                type = "In-Person",
                status = "Confirmed",
                location = "Law Office, Delhi"
            ),
            Appointment(
                id = "2",
                title = "Document Review",
                description = "Review and sign legal documents",
                lawyerName = "Adv. Priya Sharma",
                dateTime = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000), // 7 days from now
                duration = 30,
                type = "Video Call",
                status = "Pending",
                location = "Online"
            ),
            Appointment(
                id = "3",
                title = "Court Hearing Preparation",
                description = "Prepare for upcoming court hearing",
                lawyerName = "Adv. Rajesh Kumar",
                dateTime = System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000), // 2 days ago
                duration = 90,
                type = "In-Person",
                status = "Completed",
                location = "Law Office, Delhi"
            )
        ))
        appointmentsAdapter.notifyDataSetChanged()

        if (appointmentsList.isEmpty()) {
            showEmptyState(true)
        } else {
            showEmptyState(false)
        }
    }

    private fun showAppointmentDetails(appointment: Appointment) {
        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", java.util.Locale.getDefault())
        val dateTime = dateFormat.format(java.util.Date(appointment.dateTime))

        val message = """
            Lawyer: ${appointment.lawyerName}
            Date & Time: $dateTime
            Duration: ${appointment.duration} minutes
            Type: ${appointment.type}
            Location: ${appointment.location}
            Status: ${appointment.status}
            
            ${appointment.description}
        """.trimIndent()

        android.app.AlertDialog.Builder(this)
            .setTitle(appointment.title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .setNegativeButton(if (appointment.status == "Confirmed") "Cancel Appointment" else null) { _, _ ->
                cancelAppointment(appointment)
            }
            .show()
    }

    private fun showNewAppointmentDialog() {
        Toast.makeText(this, "New Appointment - Coming Soon!", Toast.LENGTH_SHORT).show()
        // TODO: Implement appointment scheduling
    }

    private fun cancelAppointment(appointment: Appointment) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Cancel Appointment")
            .setMessage("Are you sure you want to cancel this appointment?")
            .setPositiveButton("Yes") { _, _ ->
                Toast.makeText(this, "Appointment cancelled", Toast.LENGTH_SHORT).show()
                loadAppointments()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyState.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvAppointments.visibility = if (show) View.GONE else View.VISIBLE
    }
}
