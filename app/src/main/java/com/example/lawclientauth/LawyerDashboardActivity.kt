package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityLawyerDashboardBinding

class LawyerDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerDashboardBinding
    private lateinit var caseAdapter: DashboardCaseAdapter
    private lateinit var appointmentAdapter: DashboardAppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHeader()
        setupStats()
        setupActions()
        setupRecentCases()
        setupUpcomingAppointments()
    }

    private fun setupHeader() {
        binding.tvLawyerName.text = "Adv. Avijit Singh"
        binding.tvSpecialization.text = "Criminal & Civil Law"
    }

    private fun setupStats() {
        binding.statCases.tvTitle.text = "Cases"
        binding.statCases.tvValue.text = "14"

        binding.statClients.tvTitle.text = "Clients"
        binding.statClients.tvValue.text = "8"

        binding.statEarnings.tvTitle.text = "Earnings"
        binding.statEarnings.tvValue.text = "₹32,450"

        binding.statAppointments.tvTitle.text = "Appointments"
        binding.statAppointments.tvValue.text = "5"
    }

    private fun setupActions() {
        binding.actionAddCase.tvActionTitle.text = "Add Case"
        binding.actionAddCase.tvActionSubtitle.text = "Create new legal case"
        binding.actionAddCase.root.setOnClickListener {
            startActivity(Intent(this, CreateCaseActivity::class.java))
        }

        binding.actionMyClients.tvActionTitle.text = "My Clients"
        binding.actionMyClients.tvActionSubtitle.text = "Manage all your clients"
        
        binding.actionDocuments.tvActionTitle.text = "Documents"
        binding.actionDocuments.tvActionSubtitle.text = "Manage uploaded files"

        binding.actionAppointments.tvActionTitle.text = "Appointments"
        binding.actionAppointments.tvActionSubtitle.text = "All meeting schedules"
    }

    private fun setupRecentCases() {
        val cases = listOf(
            DashboardCaseModel("Case #124", "Property Dispute", "In Progress"),
            DashboardCaseModel("Case #116", "Criminal Appeal", "Hearing Soon"),
            DashboardCaseModel("Case #112", "Family Court Case", "Documents Pending")
        )
        caseAdapter = DashboardCaseAdapter(cases)
        binding.recyclerCases.layoutManager = LinearLayoutManager(this)
        binding.recyclerCases.adapter = caseAdapter
    }

    private fun setupUpcomingAppointments() {
        val appointments = listOf(
            DashboardAppointmentModel("Meeting with Client A", "Today - 4:00 PM"),
            DashboardAppointmentModel("Consultation with Client B", "Tomorrow - 11:30 AM"),
            DashboardAppointmentModel("Case Discussion", "Friday - 3:15 PM")
        )
        appointmentAdapter = DashboardAppointmentAdapter(appointments)
        binding.recyclerAppointments.layoutManager = LinearLayoutManager(this)
        binding.recyclerAppointments.adapter = appointmentAdapter
    }
}
