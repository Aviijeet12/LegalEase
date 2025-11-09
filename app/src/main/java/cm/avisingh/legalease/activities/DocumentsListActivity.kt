package cm.avisingh.legalease.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.adapters.DocumentsAdapter
import cm.avisingh.legalease.databinding.ActivityDocumentsListBinding
import cm.avisingh.legalease.models.UserDocument
import cm.avisingh.legalease.utils.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class DocumentsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentsListBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var documentsAdapter: DocumentsAdapter
    private val documentsList = mutableListOf<UserDocument>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        sharedPrefManager = SharedPrefManager(this)

        setupToolbar()
        setupRecyclerView()
        loadDocuments()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "My Documents"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        documentsAdapter = DocumentsAdapter(documentsList) { document ->
            showDocumentOptions(document)
        }
        binding.recyclerViewDocuments.apply {
            layoutManager = LinearLayoutManager(this@DocumentsListActivity)
            adapter = documentsAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddDocument.setOnClickListener {
            startActivity(Intent(this, DocumentUploadActivity::class.java))
        }

        binding.btnRetry.setOnClickListener {
            loadDocuments()
        }
    }

    private fun loadDocuments() {
        showLoading(true)
        
        // Load dummy documents for demonstration
        loadDummyDocuments()
        
        // Uncomment below for Firebase integration
        /*
        val userId = sharedPrefManager.getUserId()

        firestore.collection("documents")
            .whereEqualTo("userId", userId)
            .orderBy("uploadedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                documentsList.clear()
                for (doc in documents) {
                    val document = UserDocument(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        documentType = doc.getString("documentType") ?: "Other",
                        fileName = doc.getString("fileName") ?: "",
                        fileSize = doc.getLong("fileSize") ?: 0,
                        downloadUrl = doc.getString("downloadUrl") ?: "",
                        storagePath = doc.getString("storagePath") ?: "",
                        uploadedAt = doc.getLong("uploadedAt") ?: 0,
                        uploadedBy = doc.getString("uploadedBy") ?: "",
                        status = doc.getString("status") ?: "uploaded"
                    )
                    documentsList.add(document)
                }
                documentsAdapter.notifyDataSetChanged()
                showLoading(false)
                
                if (documentsList.isEmpty()) {
                    showEmptyState(true)
                } else {
                    showEmptyState(false)
                }
            }
            .addOnFailureListener { e ->
                showLoading(false)
                showError(true)
                Toast.makeText(this, "Failed to load documents: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        */
    }
    
    private fun loadDummyDocuments() {
        documentsList.clear()
        
        val currentTime = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000L
        
        documentsList.addAll(listOf(
            UserDocument(
                id = "doc1",
                title = "Property Sale Agreement",
                description = "Sale deed for residential property at Sector 15, Noida",
                documentType = "Legal Agreement",
                fileName = "property_sale_agreement.pdf",
                fileSize = 2457600, // 2.4 MB
                downloadUrl = "https://example.com/documents/property_sale.pdf",
                storagePath = "documents/property_sale.pdf",
                uploadedAt = currentTime - (2 * oneDay),
                uploadedBy = "Self",
                status = "verified"
            ),
            UserDocument(
                id = "doc2",
                title = "Employment Contract",
                description = "Employment agreement with Tech Solutions Pvt. Ltd.",
                documentType = "Contract",
                fileName = "employment_contract_2024.pdf",
                fileSize = 1536000, // 1.5 MB
                downloadUrl = "https://example.com/documents/employment.pdf",
                storagePath = "documents/employment.pdf",
                uploadedAt = currentTime - (5 * oneDay),
                uploadedBy = "Self",
                status = "uploaded"
            ),
            UserDocument(
                id = "doc3",
                title = "Divorce Petition",
                description = "Mutual consent divorce petition filed at Family Court",
                documentType = "Court Document",
                fileName = "divorce_petition.pdf",
                fileSize = 3145728, // 3 MB
                downloadUrl = "https://example.com/documents/divorce.pdf",
                storagePath = "documents/divorce.pdf",
                uploadedAt = currentTime - (7 * oneDay),
                uploadedBy = "Adv. Priya Sharma",
                status = "under_review"
            ),
            UserDocument(
                id = "doc4",
                title = "Aadhar Card",
                description = "Identity proof - Aadhar Card copy",
                documentType = "Identity Proof",
                fileName = "aadhar_card.pdf",
                fileSize = 512000, // 500 KB
                downloadUrl = "https://example.com/documents/aadhar.pdf",
                storagePath = "documents/aadhar.pdf",
                uploadedAt = currentTime - (10 * oneDay),
                uploadedBy = "Self",
                status = "verified"
            ),
            UserDocument(
                id = "doc5",
                title = "Rental Agreement",
                description = "11-month rental agreement for flat in Mumbai",
                documentType = "Legal Agreement",
                fileName = "rental_agreement.pdf",
                fileSize = 1843200, // 1.8 MB
                downloadUrl = "https://example.com/documents/rental.pdf",
                storagePath = "documents/rental.pdf",
                uploadedAt = currentTime - (15 * oneDay),
                uploadedBy = "Self",
                status = "verified"
            ),
            UserDocument(
                id = "doc6",
                title = "Will and Testament",
                description = "Last will and testament document registered with notary",
                documentType = "Legal Document",
                fileName = "will_testament.pdf",
                fileSize = 2048000, // 2 MB
                downloadUrl = "https://example.com/documents/will.pdf",
                storagePath = "documents/will.pdf",
                uploadedAt = currentTime - (20 * oneDay),
                uploadedBy = "Adv. Rajesh Kumar",
                status = "verified"
            ),
            UserDocument(
                id = "doc7",
                title = "Court Summons",
                description = "Summons received for case hearing on 25th Nov 2025",
                documentType = "Court Document",
                fileName = "court_summons.pdf",
                fileSize = 768000, // 750 KB
                downloadUrl = "https://example.com/documents/summons.pdf",
                storagePath = "documents/summons.pdf",
                uploadedAt = currentTime - (3 * oneDay),
                uploadedBy = "District Court",
                status = "urgent"
            ),
            UserDocument(
                id = "doc8",
                title = "Income Tax Returns 2023-24",
                description = "ITR filed for financial year 2023-24",
                documentType = "Tax Document",
                fileName = "itr_2023_24.pdf",
                fileSize = 1228800, // 1.2 MB
                downloadUrl = "https://example.com/documents/itr.pdf",
                storagePath = "documents/itr.pdf",
                uploadedAt = currentTime - (30 * oneDay),
                uploadedBy = "Self",
                status = "verified"
            ),
            UserDocument(
                id = "doc9",
                title = "Partnership Deed",
                description = "Partnership deed for business venture registration",
                documentType = "Legal Agreement",
                fileName = "partnership_deed.pdf",
                fileSize = 2764800, // 2.7 MB
                downloadUrl = "https://example.com/documents/partnership.pdf",
                storagePath = "documents/partnership.pdf",
                uploadedAt = currentTime - (45 * oneDay),
                uploadedBy = "Adv. Arjun Patel",
                status = "verified"
            ),
            UserDocument(
                id = "doc10",
                title = "Medical Reports",
                description = "Medical examination reports for insurance claim",
                documentType = "Medical Document",
                fileName = "medical_reports.pdf",
                fileSize = 4194304, // 4 MB
                downloadUrl = "https://example.com/documents/medical.pdf",
                storagePath = "documents/medical.pdf",
                uploadedAt = currentTime - (12 * oneDay),
                uploadedBy = "Self",
                status = "uploaded"
            ),
            UserDocument(
                id = "doc11",
                title = "Vehicle Registration Certificate",
                description = "RC book for Honda City - DL 3C XX 1234",
                documentType = "Vehicle Document",
                fileName = "vehicle_rc.pdf",
                fileSize = 819200, // 800 KB
                downloadUrl = "https://example.com/documents/vehicle_rc.pdf",
                storagePath = "documents/vehicle_rc.pdf",
                uploadedAt = currentTime - (8 * oneDay),
                uploadedBy = "Self",
                status = "verified"
            ),
            UserDocument(
                id = "doc12",
                title = "Power of Attorney",
                description = "General power of attorney for property matters",
                documentType = "Legal Document",
                fileName = "power_of_attorney.pdf",
                fileSize = 1638400, // 1.6 MB
                downloadUrl = "https://example.com/documents/poa.pdf",
                storagePath = "documents/poa.pdf",
                uploadedAt = currentTime - (25 * oneDay),
                uploadedBy = "Adv. Rajesh Kumar",
                status = "verified"
            )
        ))
        
        documentsAdapter.notifyDataSetChanged()
        showLoading(false)
        showEmptyState(false)
    }

    private fun showDocumentOptions(document: UserDocument) {
        val options = arrayOf("View/Download", "Delete", "Share", "Cancel")
        
        android.app.AlertDialog.Builder(this)
            .setTitle(document.title)
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> viewDocument(document)
                    1 -> deleteDocument(document)
                    2 -> shareDocument(document)
                }
            }
            .show()
    }

    private fun viewDocument(document: UserDocument) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(document.downloadUrl)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDocument(document: UserDocument) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Delete Document")
            .setMessage("Are you sure you want to delete ${document.title}?")
            .setPositiveButton("Delete") { _, _ ->
                firestore.collection("documents")
                    .document(document.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Document deleted", Toast.LENGTH_SHORT).show()
                        loadDocuments()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun shareDocument(document: UserDocument) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, document.title)
            putExtra(Intent.EXTRA_TEXT, "Document: ${document.title}\n\n${document.description}\n\nDownload: ${document.downloadUrl}")
        }
        startActivity(Intent.createChooser(shareIntent, "Share Document"))
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerViewDocuments.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        binding.layoutEmpty.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerViewDocuments.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(show: Boolean) {
        binding.layoutError.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerViewDocuments.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        loadDocuments()
    }
}
