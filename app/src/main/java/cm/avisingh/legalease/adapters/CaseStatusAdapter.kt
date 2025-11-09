package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemCaseStatusBinding
import cm.avisingh.legalease.models.CaseStatus
import java.text.SimpleDateFormat
import java.util.*

class CaseStatusAdapter(
    private val cases: List<CaseStatus>,
    private val onCaseClick: (CaseStatus) -> Unit
) : RecyclerView.Adapter<CaseStatusAdapter.CaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val binding = ItemCaseStatusBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.bind(cases[position])
    }

    override fun getItemCount() = cases.size

    inner class CaseViewHolder(
        private val binding: ItemCaseStatusBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(caseStatus: CaseStatus) {
            binding.tvCaseTitle.text = caseStatus.title
            binding.tvCaseNumber.text = caseStatus.caseNumber
            binding.tvCategory.text = caseStatus.category
            binding.tvStatus.text = caseStatus.status
            binding.tvLawyerName.text = "Lawyer: ${caseStatus.lawyerName}"
            binding.tvLastUpdated.text = "Updated: ${formatDate(caseStatus.lastUpdated)}"

            // Set status color
            val statusColor = when (caseStatus.status.lowercase()) {
                "completed" -> android.graphics.Color.parseColor("#4CAF50")
                "in progress" -> android.graphics.Color.parseColor("#2196F3")
                "pending" -> android.graphics.Color.parseColor("#FF9800")
                else -> android.graphics.Color.parseColor("#757575")
            }
            binding.tvStatus.setTextColor(statusColor)

            binding.root.setOnClickListener {
                onCaseClick(caseStatus)
            }
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}
