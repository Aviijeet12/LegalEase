package com.example.lawclientauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivityPaymentHistoryBinding
import com.example.lawclientauth.databinding.DialogCardPaymentBinding
import java.text.NumberFormat
import java.util.*

class PaymentHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentHistoryBinding
    private lateinit var adapter: PaymentAdapter
    private val sampleInvoiceUrl = "/mnt/data/le-z.zip" // uploaded file path (will be transformed to URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSummary()
        setupRecycler()
        setupClicks()
    }

    private fun setupSummary() {
        // sample totals (frontend-only)
        val totalPaid = 4500
        val nf = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        binding.tvTotalPaid.text = nf.format(totalPaid)
        binding.tvPendingCount.text = "2 pending"
    }

    private fun setupRecycler() {
        val payments = listOf(
            PaymentModel("INV-2025-001", "Adv. A Sharma", "Consultation", 1200, "2025-01-12", "Completed", sampleInvoiceUrl),
            PaymentModel("INV-2025-002", "Adv. R Mehra", "Case Filing", 2500, "2025-02-02", "Completed", sampleInvoiceUrl),
            PaymentModel("INV-2025-003", "Adv. S Kapoor", "Retainer", 800, "2025-03-11", "Pending", sampleInvoiceUrl)
        )
        adapter = PaymentAdapter(payments) { payment ->
            onPaymentClicked(payment)
        }
        binding.recyclerPayments.layoutManager = LinearLayoutManager(this)
        binding.recyclerPayments.adapter = adapter
    }

    private fun setupClicks() {
        binding.btnAddPayment.setOnClickListener {
            showCardPaymentDialog()
        }

        binding.btnFilter.setOnClickListener {
            // frontend-only: simple filter options dialog
            val items = arrayOf("All", "Completed", "Pending")
            AlertDialog.Builder(this)
                .setTitle("Filter payments")
                .setItems(items) { _, which ->
                    Toast.makeText(this, "Filter: ${items[which]}", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    private fun onPaymentClicked(payment: PaymentModel) {
        // show details + option to download invoice
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment: ${payment.invoiceId}")
        builder.setMessage(
            "Lawyer: ${payment.lawyer}\n" +
                    "Type: ${payment.type}\n" +
                    "Amount: ₹${payment.amount}\n" +
                    "Date: ${payment.date}\n" +
                    "Status: ${payment.status}"
        )
        builder.setPositiveButton("Download Invoice") { _, _ ->
            openInvoice(payment.invoiceUrl)
        }
        builder.setNegativeButton("Close", null)
        builder.show()
    }

    private fun openInvoice(urlPath: String) {
        // FRONTEND: open the provided local path as a Uri
        // Your pipeline/tool will transform the path into a real URL for the app.
        try {
            val uri = Uri.parse(urlPath)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "*/*")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open invoice. Path: $urlPath", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCardPaymentDialog() {
        // Inflate a binding-based dialog to capture card details (frontend only)
        val dialogBinding = DialogCardPaymentBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.btnPay.setOnClickListener {
            val card = dialogBinding.etCardNumber.text.toString().trim()
            val expiry = dialogBinding.etExpiry.text.toString().trim()
            val cvv = dialogBinding.etCvv.text.toString().trim()
            val amountStr = dialogBinding.etAmount.text.toString().trim()

            if (card.length < 12 || expiry.isEmpty() || cvv.length < 3 || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill valid card details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mock payment success flow
            Toast.makeText(this, "Payment successful (mock)", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
