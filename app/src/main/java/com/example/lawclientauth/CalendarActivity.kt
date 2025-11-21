package com.example.lawclientauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Premium custom calendar (grid) with appointment markers.
 * - 7-column grid
 * - Today highlight
 * - Marked booked dates (gold dot)
 * - Click day -> appointment list updates
 *
 * Frontend only; backend integration to fetch real bookings later.
 */
class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var appointmentAdapter: AppointmentAdapter

    // Mock appointment storage keyed by "yyyy-MM-dd"
    private val appointmentsByDate = mutableMapOf<String, MutableList<AppointmentModel>>()

    private var currentCalendar = Calendar.getInstance()
    private val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val sdfTitle = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareMockAppointments()
        setupMonthTitle()
        setupCalendarGrid()
        setupAppointmentsRecycler()
        setupNavigation()
    }

    private fun prepareMockAppointments() {
        // Use the uploaded path as mock document URL
        val sampleDoc = "/mnt/data/le-z.zip"

        // put some mock appointments on a few dates
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val k1 = sdfKey.format(cal.time)
        appointmentsByDate[k1] = mutableListOf(
            AppointmentModel("Consultation — Property", "11:00 AM", "Adv. A Sharma", "Upcoming", sampleDoc),
            AppointmentModel("Review Documents", "03:00 PM", "Adv. S Kapoor", "Upcoming", sampleDoc)
        )

        cal.add(Calendar.DAY_OF_MONTH, 2)
        val k2 = sdfKey.format(cal.time)
        appointmentsByDate[k2] = mutableListOf(
            AppointmentModel("Follow-up Call", "10:00 AM", "Adv. R Mehra", "Upcoming", sampleDoc)
        )

        // Today example
        val todayKey = sdfKey.format(Date())
        appointmentsByDate[todayKey] = mutableListOf(
            AppointmentModel("Preparation Meeting", "02:00 PM", "Adv. A Sharma", "Upcoming", sampleDoc)
        )
    }

    private fun setupMonthTitle() {
        binding.tvMonth.text = sdfTitle.format(currentCalendar.time)
    }

    private fun setupCalendarGrid() {
        // Build day models for the current month view (including leading/trailing days)
        val days = buildDaysForMonth(currentCalendar)
        calendarAdapter = CalendarAdapter(days) { day ->
            if (day.fullDateKey != null) {
                updateAppointmentListForDate(day.fullDateKey)
                calendarAdapter.setSelectedDate(day.fullDateKey)
            }
        }
        binding.recyclerCalendar.layoutManager = GridLayoutManager(this, 7)
        binding.recyclerCalendar.adapter = calendarAdapter

        // Initially select today if in view otherwise first day of month
        val todayKey = sdfKey.format(Date())
        calendarAdapter.setSelectedDate(todayKey)
        updateAppointmentListForDate(todayKey)
    }

    private fun buildDaysForMonth(cal: Calendar): List<DayModel> {
        val list = mutableListOf<DayModel>()

        val working = cal.clone() as Calendar
        working.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = working.get(Calendar.DAY_OF_WEEK) // 1..7 Sun..Sat
        // We want grid to start on Sunday (or you can adjust)
        val leading = firstDayOfWeek - Calendar.SUNDAY // number of prev-month days

        // get prev month days
        val prev = working.clone() as Calendar
        prev.add(Calendar.MONTH, -1)
        val prevMax = prev.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Add leading days (from previous month)
        for (i in 0 until leading) {
            val day = prevMax - leading + 1 + i
            // mark as not current month
            val dcal = Calendar.getInstance()
            dcal.time = working.time
            dcal.add(Calendar.MONTH, -1)
            dcal.set(Calendar.DAY_OF_MONTH, day)
            val key = sdfKey.format(dcal.time)
            list.add(DayModel(day = day, isCurrentMonth = false, fullDateKey = key, hasAppointment = appointmentsByDate.containsKey(key), isToday = isTodayKey(key)))
        }

        // Add current month days
        val maxDay = working.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (d in 1..maxDay) {
            val dcal = working.clone() as Calendar
            dcal.set(Calendar.DAY_OF_MONTH, d)
            val key = sdfKey.format(dcal.time)
            list.add(DayModel(day = d, isCurrentMonth = true, fullDateKey = key, hasAppointment = appointmentsByDate.containsKey(key), isToday = isTodayKey(key)))
        }

        // trailing days to fill the grid to multiple of 7
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

    private fun updateAppointmentListForDate(key: String) {
        binding.tvSelectedDate.text = "Appointments on ${key}"
        val list = appointmentsByDate[key] ?: emptyList()
        appointmentAdapter.updateList(list)
    }

    private fun setupAppointmentsRecycler() {
        appointmentAdapter = AppointmentAdapter(emptyList()) { /* click handler if needed */ }
        binding.recyclerAppointments.layoutManager = LinearLayoutManager(this)
        binding.recyclerAppointments.adapter = appointmentAdapter
    }

    private fun setupNavigation() {
        binding.btnPrev.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            binding.tvMonth.text = sdfTitle.format(currentCalendar.time)
            calendarAdapter.updateDays(buildDaysForMonth(currentCalendar))
        }
        binding.btnNext.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            binding.tvMonth.text = sdfTitle.format(currentCalendar.time)
            calendarAdapter.updateDays(buildDaysForMonth(currentCalendar))
        }

        binding.btnAddAppointment.setOnClickListener {
            startActivity(android.content.Intent(this, BookAppointmentActivity::class.java))
        }
    }
}
