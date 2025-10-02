package com.nyayasetu.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nyayasetu.app.firebase.AuthManager
import com.nyayasetu.app.payment.PaymentManager

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val lawyerIdEditText = findViewById<EditText>(R.id.editTextLawyerId)
        val amountEditText = findViewById<EditText>(R.id.editTextAmount)
        val payButton = findViewById<Button>(R.id.buttonPay)

        payButton.setOnClickListener {
            val lawyerId = lawyerIdEditText.text.toString().trim()
            val amountText = amountEditText.text.toString().trim()
            val amount = amountText.toDoubleOrNull()
            val user = AuthManager.getCurrentUser()
            if (lawyerId.isEmpty()) {
                Toast.makeText(this, "Lawyer ID cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Enter a valid payment amount.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (user != null) {
                PaymentManager.makePayment(user.uid, lawyerId, amount) { success, transactionIdOrError ->
                    if (success) {
                        Toast.makeText(this, "Payment successful! Transaction ID: $transactionIdOrError", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Payment failed: $transactionIdOrError", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
