package com.example.lawclientauth

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemCalendarDayBinding

class CalendarAdapter(
    private var days: List<DayModel>,
    private val onClick: (DayModel) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayVH>() {

    private var selectedDateKey: String? = null

    inner class DayVH(val binding: ItemCalendarDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayVH {
        val binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayVH(binding)
    }

    override fun onBindViewHolder(holder: DayVH, position: Int) {
        val d = days[position]
        holder.binding.tvDayNumber.text = d.day.toString()

        // tone down non-current month days
        holder.binding.tvDayNumber.alpha = if (d.isCurrentMonth) 1f else 0.35f

        // today highlight
        if (d.isToday) {
            holder.binding.root.setBackgroundResource(R.drawable.bg_day_circle_today)
            holder.binding.tvDayNumber.setTextColor(Color.WHITE)
        } else if (selectedDateKey != null && d.fullDateKey == selectedDateKey) {
            // selected highlight
            holder.binding.root.setBackgroundResource(R.drawable.bg_day_circle_selected)
            holder.binding.tvDayNumber.setTextColor(Color.WHITE)
        } else {
            holder.binding.root.setBackgroundResource(0)
            holder.binding.tvDayNumber.setTextColor(Color.parseColor("#0B2F4E"))
        }

        // appointment indicator (gold dot)
        holder.binding.viewDot.alpha = if (d.hasAppointment) 1f else 0f

        holder.binding.root.setOnClickListener {
            onClick(d)
            selectedDateKey = d.fullDateKey
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = days.size

    fun setSelectedDate(key: String?) {
        selectedDateKey = key
        notifyDataSetChanged()
    }

    fun updateDays(newDays: List<DayModel>) {
        this.days = newDays
        notifyDataSetChanged()
    }
}
