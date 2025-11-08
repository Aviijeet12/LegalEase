package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.R
import cm.avisingh.legalease.models.CaseUpdate

class CaseUpdateAdapter : RecyclerView.Adapter<CaseUpdateAdapter.CaseUpdateViewHolder>() {

    private val updates: MutableList<CaseUpdate> = mutableListOf()

    inner class CaseUpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUpdateTitle: TextView = itemView.findViewById(R.id.tvUpdateTitle)
        private val tvUpdateDescription: TextView = itemView.findViewById(R.id.tvUpdateDescription)
        private val tvUpdateDate: TextView = itemView.findViewById(R.id.tvUpdateDate)
        private val tvUpdateType: TextView = itemView.findViewById(R.id.tvUpdateType)

        fun bind(update: CaseUpdate) {
            tvUpdateTitle.text = update.title
            tvUpdateDescription.text = update.description
            tvUpdateDate.text = update.getFormattedDate()
            tvUpdateType.text = update.type

            // Set type background color
            tvUpdateType.setBackgroundColor(
                ContextCompat.getColor(itemView.context, update.getTypeColor())
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseUpdateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_case_update, parent, false)
        return CaseUpdateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseUpdateViewHolder, position: Int) {
        holder.bind(updates[position])
    }

    override fun getItemCount(): Int = updates.size

    fun updateUpdates(newUpdates: List<CaseUpdate>) {
        updates.clear()
        updates.addAll(newUpdates)
        notifyDataSetChanged()
    }
}