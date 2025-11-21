package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemSearchResultBinding

class SearchAdapter(
    private var list: List<SearchResultModel>,
    private val onOpenLawyer: (LawyerModel) -> Unit,
    private val onOpenCase: (CaseModel) -> Unit,
    private val onOpenDocument: (DocumentModel) -> Unit,
    private val onOpenAppointment: (AppointmentModel) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SVH>() {

    inner class SVH(val b: ItemSearchResultBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SVH {
        val b = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SVH(b)
    }

    override fun onBindViewHolder(holder: SVH, position: Int) {
        val item = list[position]
        holder.b.tvTitle.text = item.title
        holder.b.tvSubtitle.text = item.subtitle ?: ""

        // clear buttons
        holder.b.btnPrimary.visibility = View.GONE
        holder.b.btnSecondary.visibility = View.GONE

        when (item.type) {
            "lawyer" -> {
                val l = item.extra as LawyerModel
                holder.b.ivType.setImageResource(R.drawable.ic_profile_placeholder)
                holder.b.tvType.text = "LAWYER"
                holder.b.btnPrimary.visibility = View.VISIBLE
                holder.b.btnPrimary.text = "View Profile"
                holder.b.btnPrimary.setOnClickListener { onOpenLawyer(l) }
                holder.b.btnSecondary.visibility = View.VISIBLE
                holder.b.btnSecondary.text = "Chat"
                holder.b.btnSecondary.setOnClickListener {
                    // open chat
                    holder.b.root.context.startActivity(android.content.Intent(holder.b.root.context, ChatActivity::class.java))
                }
            }
            "case" -> {
                val c = item.extra as CaseModel
                holder.b.ivType.setImageResource(R.drawable.ic_case)
                holder.b.tvType.text = "CASE"
                holder.b.btnPrimary.visibility = View.VISIBLE
                holder.b.btnPrimary.text = "Open Case"
                holder.b.btnPrimary.setOnClickListener { onOpenCase(c) }
                holder.b.btnSecondary.visibility = View.VISIBLE
                holder.b.btnSecondary.text = "Docs"
                holder.b.btnSecondary.setOnClickListener {
                    // open case details (documents section)
                    onOpenCase(c)
                }
            }
            "document" -> {
                val d = item.extra as DocumentModel
                holder.b.ivType.setImageResource(R.drawable.ic_document)
                holder.b.tvType.text = "DOCUMENT"
                holder.b.btnPrimary.visibility = View.VISIBLE
                holder.b.btnPrimary.text = "Open"
                holder.b.btnPrimary.setOnClickListener { onOpenDocument(d) }
                holder.b.btnSecondary.visibility = View.VISIBLE
                holder.b.btnSecondary.text = "Case"
                holder.b.btnSecondary.setOnClickListener {
                    // find case by id and open if exists
                    val case = (holder.b.root.context as? SearchActivity)?.let { act ->
                        act.findCaseById(d.caseId)
                    }
                    case?.let { onOpenCase(it) }
                }
            }
            "appointment" -> {
                val a = item.extra as AppointmentModel
                holder.b.ivType.setImageResource(R.drawable.ic_calendar)
                holder.b.tvType.text = "APPT"
                holder.b.btnPrimary.visibility = View.VISIBLE
                holder.b.btnPrimary.text = "Open"
                holder.b.btnPrimary.setOnClickListener { onOpenAppointment(a) }
                holder.b.btnSecondary.visibility = View.VISIBLE
                holder.b.btnSecondary.text = "Chat"
                holder.b.btnSecondary.setOnClickListener {
                    holder.b.root.context.startActivity(android.content.Intent(holder.b.root.context, ChatActivity::class.java))
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<SearchResultModel>) {
        (list as? MutableList)?.clear()
        list = newList
        notifyDataSetChanged()
    }
}
