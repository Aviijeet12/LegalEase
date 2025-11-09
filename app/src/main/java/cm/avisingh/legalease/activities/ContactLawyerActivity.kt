package cm.avisingh.legalease.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityContactLawyerBinding

class ContactLawyerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactLawyerBinding
    private var lawyerName: String = "Adv. Rajesh Kumar"
    private var lawyerEmail: String = "rajesh.kumar@legalease.com"
    private var lawyerPhone: String = "+919876543210"
    private var lawyerSpecialization: String = "Property & Civil Law"
    private var lawyerExperience: String = "15+ years"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactLawyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get lawyer details from intent
        lawyerName = intent.getStringExtra("lawyer_name") ?: "Adv. Rajesh Kumar"
        lawyerEmail = intent.getStringExtra("lawyer_email") ?: "rajesh.kumar@legalease.com"
        lawyerPhone = intent.getStringExtra("lawyer_phone") ?: "+919876543210"
        lawyerSpecialization = intent.getStringExtra("lawyer_specialization") ?: "Property & Civil Law"
        lawyerExperience = intent.getStringExtra("lawyer_experience") ?: "15+ years"

        setupToolbar()
        setupLawyerInfo()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Contact Lawyer"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupLawyerInfo() {
        // Display selected lawyer information
        binding.tvLawyerName.text = lawyerName
        binding.tvSpecialization.text = lawyerSpecialization
        binding.tvExperience.text = "$lawyerExperience experience"
        binding.tvEmail.text = lawyerEmail
        binding.tvPhone.text = lawyerPhone
    }

    private fun setupClickListeners() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
            } else {
                binding.etMessage.error = "Please enter a message"
            }
        }

        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$lawyerPhone")
            }
            startActivity(intent)
        }

        binding.btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$lawyerEmail")
                putExtra(Intent.EXTRA_SUBJECT, "Legal Consultation Request")
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }

        binding.btnSchedule.setOnClickListener {
            startActivity(Intent(this, AppointmentsActivity::class.java))
        }
    }

    private fun sendMessage(message: String) {
        // TODO: Implement Firebase Firestore messaging
        Toast.makeText(this, "Message sent to lawyer!", Toast.LENGTH_SHORT).show()
        binding.etMessage.text?.clear()
        
        // Show confirmation dialog
        android.app.AlertDialog.Builder(this)
            .setTitle("Message Sent")
            .setMessage("Your message has been sent to $lawyerName. You will receive a response within 24 hours.")
            .setPositiveButton("OK", null)
            .show()
    }
}
