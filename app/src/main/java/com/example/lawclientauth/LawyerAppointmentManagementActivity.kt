package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityLawyerAppointmentManagementBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Lawyer Appointment Management Screen (frontend only)
 * - Shows incoming appointment requests (Accept / Reject)
 * - Mini calendar (uses CalendarAdapter from earlier) to view bookings
 * - Upcoming meetings list with details and quick actions
 */
class LawyerAppointmentManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerAppointmentManagementBinding

    // Requests adapter, upcoming adapter, calendar adapter (reuse existing CalendarAdapter + DayModel)
    private lateinit var requestAdapter: AppointmentRequestAdapter
    private lateinit var upcomingAdapter: DashboardAppointmentAdapter
    private lateinit var calendarAdapter: CalendarAdapter

    private val appointmentRequests = mutableListOf<AppointmentRequestModel>()
    private val upcomingAppointments = mutableListOf<DashboardAppointmentModel>()
    private val appointmentsByDate = mutableMapOf<String, MutableList<AppointmentRequestModel>>()

    private val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var currentCalendar = Calendar.getInstance()

    private val sampleDocUrl = "/mnt/data/le-z.zip" // developer instruction: use uploaded file path

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerAppointmentManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMockData()
        setupRequestsRecycler()
        setupUpcomingRecycler()
        setupMiniCalendar()
        setupFilter()
    }

    private fun loadMockData() {
        // mock appointment requests
        appointmentRequests.clear()
        appointmentRequests.add(
            AppointmentRequestModel(
                id = "req1",
                clientName = "Rahul Verma",
                date = futureDate(1),
                time = "11:00 AM",
                meetingType = "Video Call",
                reason = "Discuss property dispute",
                clientDocs = listOf(sampleDocUrl),
                status = "Pending"
            )
        )
        appointmentRequests.add(
            AppointmentRequestModel(
                id = "req2",
                clientName = "Aditi Sharma",
                date = futureDate(2),
                time = "02:30 PM",
                meetingType = "In-Person",
                reason = "Contract review",
                clientDocs = emptyList(),
                status = "Pending"
            )
        )

        // upcoming meetings
        upcomingAppointments.clear()
        upcomingAppointments.add(DashboardAppointmentModel("Meeting with Rahul Verma", "Tomorrow - 11:00 AM"))
        upcomingAppointments.add(DashboardAppointmentModel("Consultation — Aditi", "In 2 days - 02:30 PM"))

        // map appointmentsByDate for mini calendar markers (key: yyyy-MM-dd)
        appointmentsByDate.clear()
        appointmentRequests.forEach { req ->
            val list = appointmentsByDate.getOrPut(req.date) { mutableListOf() }
            list.add(req)
        }
        // also add upcoming as appointmentsByDate (for visualization)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 3)
        appointmentsByDate[sdfKey.format(cal.time)] = mutableListOf(
            AppointmentRequestModel("req3", "Nikhil", sdfKey.format(cal.time), "03:00 PM", "Audio Call", "Follow-up", emptyList(), "Accepted")
        )
    }

    private fun setupRequestsRecycler() {
        requestAdapter = AppointmentRequestAdapter(appointmentRequests,
            onAccept = { req ->
                req.status = "Accepted"
                // move to upcoming list
                upcomingAppointments.add(DashboardAppointmentModel("Meeting with ${req.clientName}", "${req.date} - ${req.time}"))
                // update calendar markers
                val list = appointmentsByDate.getOrPut(req.date) { mutableListOf() }
                list.add(req)
                requestAdapter.notifyDataSetChanged()
                upcomingAdapter.notifyDataSetChanged()
                calendarAdapter.updateDays(buildDaysForMonth(currentCalendar))
                Toast.makeText(this, "Accepted request from ${req.clientName} (UI only)", Toast.LENGTH_SHORT).show()
            },
            onReject = { req ->
                req.status = "Rejected"
                requestAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Rejected request from ${req.clientName}", Toast.LENGTH_SHORT).show()
            },
            onViewClient = { req ->
                // navigate to client profile
                startActivity(Intent(this, ClientProfileActivity::class.java))
            }
        )

        binding.recyclerRequests.layoutManager = LinearLayoutManager(this)
        binding.recyclerRequests.adapter = requestAdapter
    }

    private fun setupUpcomingRecycler() {
        upcomingAdapter = DashboardAppointmentAdapter(upcomingAppointments)
        binding.recyclerUpcoming.layoutManager = LinearLayoutManager(this)
        binding.recyclerUpcoming.adapter = upcomingAdapter
    }

    private fun setupMiniCalendar() {
        // Build days and reuse CalendarAdapter (created earlier)
        val days = buildDaysForMonth(currentCalendar)
        calendarAdapter = CalendarAdapter(days) { day ->
            // when date tapped: show list of requests/appointments for that day
            val dateKey = day.fullDateKey
            if (dateKey != null) {
                val list = appointmentsByDate[dateKey] ?: emptyList()
                // show modal or quick toast for now
                if (list.isEmpty()) {
                    Toast.makeText(this, "No appointments on $dateKey", Toast.LENGTH_SHORT).show()
                } else {
                    // open a small detail screen listing them (reuse a simple Activity)
                    val intent = Intent(this, AppointmentsForDateActivity::class.java)
                    intent.putExtra("dateKey", dateKey)
                    startActivity(intent)
                }
            }
        }
        binding.recyclerMiniCalendar.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 7)
        binding.recyclerMiniCalendar.adapter = calendarAdapter
    }

    private fun setupFilter() {
        val filters = listOf("All", "Pending", "Accepted", "Rejected")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filters)
        binding.dropdownFilter.setAdapter(adapter)
        binding.dropdownFilter.setOnItemClickListener { _, _, _, _ ->
            val sel = binding.dropdownFilter.text.toString()
            if (sel == "All") requestAdapter.updateList(appointmentRequests)
            else requestAdapter.updateList(appointmentRequests.filter { it.status == sel })
        }
    }

    // utility to build the day models for current month (similar to CalendarActivity)
    private fun buildDaysForMonth(cal: Calendar): List<DayModel> {
        val list = mutableListOf<DayModel>()
        val working = cal.clone() as Calendar
        working.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = working.get(Calendar.DAY_OF_WEEK)
        val leading = firstDayOfWeek - Calendar.SUNDAY

        val prev = working.clone() as Calendar
        prev.add(Calendar.MONTH, -1)
        val prevMax = prev.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 0 until leading) {
            val day = prevMax - leading + 1 + i
            val dcal = working.clone() as Calendar
            dcal.add(Calendar.MONTH, -1)
            dcal.set(Calendar.DAY_OF_MONTH, day)
            val key = sdfKey.format(dcal.time)
            list.add(DayModel(day = day, isCurrentMonth = false, fullDateKey = key, hasAppointment = appointmentsByDate.containsKey(key), isToday = isTodayKey(key)))
        }

        val maxDay = working.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (d in 1..maxDay) {
            val dcal = working.clone() as Calendar
            dcal.set(Calendar.DAY_OF_MONTH, d)
            val key = sdfKey.format(dcal.time)
            list.add(DayModel(day = d, isCurrentMonth = true, fullDateKey = key, hasAppointment = appointmentsByDate.containsKey(key), isToday = isTodayKey(key)))
        }

        val trailing = (7 - (list.size % 7)) % 7
        for (i in 1..trailing) {
            val dcal = working.clone() as Calendar
            dcal.add(Calendar.MONTH, 1)
            dcal.set(Calendar.DAY_OF_MONTH, i)
            val key = sdfKey.format(dcal.time)
            list.add(DayModel(day = i, isCurrentMonth = false, fullDateKey = key, hasAppointment = appointmentsByDate.containsKey(key), isToday = isTodayKey(key)))
        }
        return list
    }

    private fun isTodayKey(key: String): Boolean {
        val today = sdfKey.format(Date())
        return today == key
    }

    // helper: get date string for n days ahead
    private fun futureDate(offsetDays: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, offsetDays)
        return sdfKey.format(cal.time)
    }
}
