package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.models.Case
import java.text.SimpleDateFormat
import java.util.*

class CaseAdapter(
    private val cases: MutableList<Case>,
    private val onCaseClick: (Case) -> Unit
) : RecyclerView.Adapter<CaseAdapter.CaseViewHolder>() {

    inner class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCaseNumber: TextView = itemView.findViewById(R.id.tvCaseNumber)
        private val tvCaseTitle: TextView = itemView.findViewById(R.id.tvCaseTitle)
        private val tvClientName: TextView = itemView.findViewById(R.id.tvClientName)
        private val tvCaseType: TextView = itemView.findViewById(R.id.tvCaseType)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvNextHearing: TextView = itemView.findViewById(R.id.tvNextHearing)
        private val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)

        fun bind(case: Case) {
            tvCaseNumber.text = case.caseNumber
            tvCaseTitle.text = case.title
            tvClientName.text = case.clientName
            tvCaseType.text = case.caseType

            // Status with color
            tvStatus.text = case.status
            tvStatus.setBackgroundColor(ContextCompat.getColor(itemView.context, case.getStatusColor()))

            // Priority with color
            tvPriority.text = case.priority
            tvPriority.setBackgroundColor(ContextCompat.getColor(itemView.context, case.getPriorityColor()))

            // Next hearing date
            case.nextHearingDate?.let { date ->
                tvNextHearing.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                tvNextHearing.visibility = View.VISIBLE
            } ?: run {
                tvNextHearing.visibility = View.GONE
            }

            // Click listener
            itemView.setOnClickListener {
                onCaseClick(case)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_case, parent, false)
        return CaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(cases[position])
    }

    override fun getItemCount(): Int = cases.size

    fun updateCases(newCases: List<Case>) {
        cases.clear()
        cases.addAll(newCases)
        notifyDataSetChanged()
    }

    fun filterCases(status: String) {
        val filtered = if (status == "All") {
            cases
        } else {
            cases.filter { it.status == status }
        }
        updateCases(filtered)
    }
}