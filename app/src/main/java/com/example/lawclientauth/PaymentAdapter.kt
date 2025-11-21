package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.ItemPaymentBinding

class PaymentAdapter(
    private val list: List<PaymentModel>,
    private val onClick: (PaymentModel) -> Unit
) : RecyclerView.Adapter<PaymentAdapter.PaymentVH>() {

    inner class PaymentVH(val binding: ItemPaymentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentVH {
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentVH(binding)
    }

    override fun onBindViewHolder(holder: PaymentVH, position: Int) {
        val p = list[position]
        holder.binding.tvInvoiceId.text = p.invoiceId
        holder.binding.tvLawyer.text = p.lawyer
        holder.binding.tvType.text = p.type
        holder.binding.tvAmount.text = "₹${p.amount}"
        holder.binding.tvDate.text = p.date
        holder.binding.tvStatus.text = p.status

        holder.binding.btnDownload.setOnClickListener {
            // open invoice URL (frontend only)
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(p.invoiceUrl), "*/*")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                holder.itemView.context.startActivity(intent)
            } catch (e: Exception) {
                // fallback to callback for custom handling
                onClick(p)
            }
        }

        holder.binding.root.setOnClickListener {
            onClick(p)
        }
    }

    override fun getItemCount() = list.size
}
