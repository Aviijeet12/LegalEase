package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ItemLawyerCaseBinding
import cm.avisingh.legalease.models.LawyerCase
import java.text.SimpleDateFormat
import java.util.*

class LawyerCasesAdapter(
    private val cases: List<LawyerCase>,
    private val onCaseClick: (LawyerCase) -> Unit
) : RecyclerView.Adapter<LawyerCasesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLawyerCaseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(case: LawyerCase) {
            binding.apply {
                tvCaseNumber.text = case.caseNumber
                tvCaseTitle.text = case.title
                tvClientName.text = "Client: ${case.clientName}"
                tvCaseType.text = case.caseType
                tvCourt.text = case.court

                // Format next hearing date
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                tvNextHearing.text = "Next Hearing: ${dateFormat.format(Date(case.nextHearing))}"

                // Set status badge
                tvStatus.text = case.status
                when (case.status) {
                    "Active" -> {
                        tvStatus.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.status_active)
                        )
                    }
                    "Urgent" -> {
                        tvStatus.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.error_red)
                        )
                    }
                    "Pending" -> {
                        tvStatus.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.status_inactive)
                        )
                    }
                    else -> {
                        tvStatus.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.gray)
                        )
                    }
                }

                // Set priority indicator
                when (case.priority) {
                    "High" -> {
                        viewPriorityIndicator.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.error_red)
                        )
                    }
                    "Medium" -> {
                        viewPriorityIndicator.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.status_busy)
                        )
                    }
                    else -> {
                        viewPriorityIndicator.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.status_active)
                        )
                    }
                }

                root.setOnClickListener { onCaseClick(case) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLawyerCaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cases[position])
    }

    override fun getItemCount() = cases.size
}
