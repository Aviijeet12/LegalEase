package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemAppointmentBinding
import cm.avisingh.legalease.models.Appointment
import java.text.SimpleDateFormat
import java.util.*

class AppointmentsAdapter(
    private val appointments: List<Appointment>,
    private val onAppointmentClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    override fun getItemCount() = appointments.size

    inner class AppointmentViewHolder(
        private val binding: ItemAppointmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) {
            binding.tvAppointmentTitle.text = appointment.title
            binding.tvLawyerName.text = appointment.lawyerName
            binding.tvDateTime.text = formatDateTime(appointment.dateTime)
            binding.tvType.text = appointment.type
            binding.tvStatus.text = appointment.status
            binding.tvLocation.text = appointment.location

            // Set status color
            val statusColor = when (appointment.status.lowercase()) {
                "confirmed" -> android.graphics.Color.parseColor("#4CAF50")
                "pending" -> android.graphics.Color.parseColor("#FF9800")
                "cancelled" -> android.graphics.Color.parseColor("#F44336")
                "completed" -> android.graphics.Color.parseColor("#757575")
                else -> android.graphics.Color.parseColor("#757575")
            }
            binding.tvStatus.setTextColor(statusColor)

            binding.root.setOnClickListener {
                onAppointmentClick(appointment)
            }
        }

        private fun formatDateTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}
