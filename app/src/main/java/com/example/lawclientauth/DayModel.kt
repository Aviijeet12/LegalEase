package com.example.lawclientauth

data class DayModel(
    val day: Int,
    val isCurrentMonth: Boolean,
    val fullDateKey: String?, // "yyyy-MM-dd"
    val hasAppointment: Boolean = false,
    val isToday: Boolean = false
)
