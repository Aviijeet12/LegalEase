package cm.avisingh.legalease.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.adapters.FAQAdapter
import cm.avisingh.legalease.databinding.ActivityHelpSupportBinding
import cm.avisingh.legalease.models.FAQ

class HelpSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpSupportBinding
    private lateinit var faqAdapter: FAQAdapter
    private val faqList = mutableListOf<FAQ>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadFAQs()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Help & Support"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadFAQs() {
        faqList.clear()
        faqList.addAll(listOf(
            FAQ(
                id = "1",
                question = "How do I upload legal documents?",
                answer = "To upload documents:\n1. Go to your dashboard\n2. Click on 'Upload Document'\n3. Select the file from your device\n4. Fill in document details\n5. Click 'Upload'\n\nSupported formats: PDF, DOC, DOCX, JPG, PNG",
                category = "Documents",
                isExpanded = false
            ),
            FAQ(
                id = "2",
                question = "How can I contact my lawyer?",
                answer = "You can contact your assigned lawyer through:\n\n• Click 'Contact Lawyer' on your dashboard\n• Send messages directly through the app\n• View lawyer's contact information\n• Schedule appointments\n\nYour lawyer will respond within 24 hours.",
                category = "Communication",
                isExpanded = false
            ),
            FAQ(
                id = "3",
                question = "How do I track my case status?",
                answer = "Track your case status by:\n\n1. Open 'Case Status' from dashboard\n2. View case timeline and updates\n3. See upcoming hearings\n4. Check document submissions\n5. Get real-time notifications\n\nYou'll receive updates whenever there's progress.",
                category = "Cases",
                isExpanded = false
            ),
            FAQ(
                id = "4",
                question = "What types of documents can I upload?",
                answer = "You can upload:\n\n• Legal Briefs\n• Contract Agreements\n• Court Filings\n• Evidence Documents\n• Client Correspondence\n• Legal Research\n• Any other legal documents\n\nMax file size: 50 MB per document",
                category = "Documents",
                isExpanded = false
            ),
            FAQ(
                id = "5",
                question = "How do I schedule an appointment?",
                answer = "To schedule an appointment:\n\n1. Go to 'Appointments' section\n2. Click 'New Appointment'\n3. Select date and time\n4. Choose appointment type\n5. Add notes if needed\n6. Submit request\n\nYour lawyer will confirm the appointment.",
                category = "Appointments",
                isExpanded = false
            ),
            FAQ(
                id = "6",
                question = "Is my data secure?",
                answer = "Yes! Your data security is our top priority:\n\n✓ End-to-end encryption\n✓ Secure cloud storage\n✓ Regular backups\n✓ GDPR compliant\n✓ Two-factor authentication\n✓ Secure file sharing\n\nAll documents are stored securely on Firebase.",
                category = "Security",
                isExpanded = false
            ),
            FAQ(
                id = "7",
                question = "How do I download my documents?",
                answer = "To download documents:\n\n1. Go to 'Documents' section\n2. Find the document you need\n3. Tap on the document\n4. Select 'View/Download'\n5. Choose download location\n\nDocuments are available anytime, even offline once downloaded.",
                category = "Documents",
                isExpanded = false
            ),
            FAQ(
                id = "8",
                question = "What if I forget my password?",
                answer = "To reset your password:\n\n1. Go to login screen\n2. Click 'Forgot Password'\n3. Enter your email\n4. Check your email for reset link\n5. Create new password\n\nIf using Google Sign-In, reset through Google account.",
                category = "Account",
                isExpanded = false
            ),
            FAQ(
                id = "9",
                question = "How do notifications work?",
                answer = "You'll receive notifications for:\n\n• Case updates\n• New messages from lawyer\n• Upcoming appointments\n• Document reviews\n• Court hearing reminders\n• Important deadlines\n\nManage notification settings in your profile.",
                category = "Notifications",
                isExpanded = false
            ),
            FAQ(
                id = "10",
                question = "Can I share documents with others?",
                answer = "Yes! You can share documents:\n\n1. Open the document\n2. Tap share icon\n3. Choose sharing method\n4. Select recipients\n\nSharing is secure and you control who has access.",
                category = "Documents",
                isExpanded = false
            )
        ))
    }

    private fun setupRecyclerView() {
        faqAdapter = FAQAdapter(faqList) { faq, position ->
            toggleFAQ(position)
        }
        binding.rvFAQs.apply {
            layoutManager = LinearLayoutManager(this@HelpSupportActivity)
            adapter = faqAdapter
        }
    }

    private fun toggleFAQ(position: Int) {
        faqList[position].isExpanded = !faqList[position].isExpanded
        faqAdapter.notifyItemChanged(position)
    }

    private fun setupClickListeners() {
        binding.cardEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@legalease.com")
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }

        binding.cardPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:+1234567890")
            }
            startActivity(intent)
        }

        binding.cardLiveChat.setOnClickListener {
            // TODO: Implement live chat
            android.widget.Toast.makeText(this, "Live Chat - Coming Soon!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}
