package cm.avisingh.legalease.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.adapters.CaseStatusAdapter
import cm.avisingh.legalease.databinding.ActivityCaseStatusBinding
import cm.avisingh.legalease.models.CaseStatus
import cm.avisingh.legalease.models.CaseUpdate
import cm.avisingh.legalease.utils.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

class CaseStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseStatusBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var caseAdapter: CaseStatusAdapter
    private val casesList = mutableListOf<CaseStatus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        sharedPrefManager = SharedPrefManager(this)

        setupToolbar()
        setupRecyclerView()
        loadCases()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "My Cases"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        caseAdapter = CaseStatusAdapter(casesList) { caseStatus ->
            showCaseDetails(caseStatus)
        }
        binding.rvCases.apply {
            layoutManager = LinearLayoutManager(this@CaseStatusActivity)
            adapter = caseAdapter
        }
    }

    private fun loadCases() {
        showLoading(true)
        val userId = sharedPrefManager.getUserId()

        firestore.collection("cases")
            .whereEqualTo("clientId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                casesList.clear()
                for (doc in documents) {
                    val caseStatus = CaseStatus(
                        id = doc.id,
                        caseNumber = doc.getString("caseNumber") ?: "",
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        status = doc.getString("status") ?: "Pending",
                        category = doc.getString("category") ?: "General",
                        lawyerId = doc.getString("lawyerId") ?: "",
                        lawyerName = doc.getString("lawyerName") ?: "Assigned Lawyer",
                        clientId = doc.getString("clientId") ?: "",
                        createdAt = doc.getLong("createdAt") ?: 0,
                        lastUpdated = doc.getLong("lastUpdated") ?: 0,
                        nextHearing = doc.getLong("nextHearing") ?: 0
                    )
                    casesList.add(caseStatus)
                }
                
                if (casesList.isEmpty()) {
                    // Add demo cases if none exist
                    addDemoCases()
                } else {
                    caseAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                
                if (casesList.isEmpty()) {
                    showEmptyState(true)
                } else {
                    showEmptyState(false)
                }
            }
            .addOnFailureListener { e ->
                showLoading(false)
                // Show demo cases on error
                addDemoCases()
                Toast.makeText(this, "Showing demo data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addDemoCases() {
        casesList.clear()
        casesList.addAll(listOf(
            CaseStatus(
                id = "demo1",
                caseNumber = "CASE-2025-001",
                title = "Property Dispute Resolution",
                description = "Boundary dispute with neighboring property",
                status = "In Progress",
                category = "Property Law",
                lawyerId = "lawyer1",
                lawyerName = "Adv. Rajesh Kumar",
                clientId = sharedPrefManager.getUserId(),
                createdAt = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000), // 30 days ago
                lastUpdated = System.currentTimeMillis() - (2L * 24 * 60 * 60 * 1000), // 2 days ago
                nextHearing = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000) // 7 days from now
            ),
            CaseStatus(
                id = "demo2",
                caseNumber = "CASE-2025-002",
                title = "Contract Review",
                description = "Employment contract verification and negotiation",
                status = "Under Review",
                category = "Employment Law",
                lawyerId = "lawyer2",
                lawyerName = "Adv. Priya Sharma",
                clientId = sharedPrefManager.getUserId(),
                createdAt = System.currentTimeMillis() - (15L * 24 * 60 * 60 * 1000),
                lastUpdated = System.currentTimeMillis() - (1L * 24 * 60 * 60 * 1000),
                nextHearing = 0
            ),
            CaseStatus(
                id = "demo3",
                caseNumber = "CASE-2024-089",
                title = "Divorce Settlement",
                description = "Mutual consent divorce proceedings",
                status = "Completed",
                category = "Family Law",
                lawyerId = "lawyer1",
                lawyerName = "Adv. Rajesh Kumar",
                clientId = sharedPrefManager.getUserId(),
                createdAt = System.currentTimeMillis() - (90L * 24 * 60 * 60 * 1000),
                lastUpdated = System.currentTimeMillis() - (5L * 24 * 60 * 60 * 1000),
                nextHearing = 0
            )
        ))
        caseAdapter.notifyDataSetChanged()
        showEmptyState(false)
    }

    private fun showCaseDetails(caseStatus: CaseStatus) {
        val updates = listOf(
            CaseUpdate(
                id = "1",
                caseId = caseStatus.id,
                title = "Case Filed",
                description = "Case has been officially filed with the court",
                date = Date(caseStatus.createdAt),
                type = "NOTE"
            ),
            CaseUpdate(
                id = "2",
                caseId = caseStatus.id,
                title = "Documents Submitted",
                description = "All required documents have been submitted",
                date = Date(caseStatus.createdAt + (5L * 24 * 60 * 60 * 1000)),
                type = "DOCUMENT"
            ),
            CaseUpdate(
                id = "3",
                caseId = caseStatus.id,
                title = "Status Update",
                description = "Case is progressing well. Preparing for next hearing.",
                date = Date(caseStatus.lastUpdated),
                type = "HEARING"
            )
        )

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle(caseStatus.title)
            .setMessage(buildCaseDetailsMessage(caseStatus, updates))
            .setPositiveButton("OK", null)
            .setNeutralButton("Contact Lawyer") { _, _ ->
                // Navigate to contact lawyer
                Toast.makeText(this, "Opening chat with ${caseStatus.lawyerName}", Toast.LENGTH_SHORT).show()
            }
            .create()
        dialog.show()
    }

    private fun buildCaseDetailsMessage(caseStatus: CaseStatus, updates: List<CaseUpdate>): String {
        val sb = StringBuilder()
        sb.append("Case Number: ${caseStatus.caseNumber}\n\n")
        sb.append("Status: ${caseStatus.status}\n")
        sb.append("Category: ${caseStatus.category}\n")
        sb.append("Lawyer: ${caseStatus.lawyerName}\n\n")
        
        if (caseStatus.nextHearing > 0) {
            val hearingDate = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                .format(java.util.Date(caseStatus.nextHearing))
            sb.append("Next Hearing: $hearingDate\n\n")
        }
        
        sb.append("Recent Updates:\n\n")
        updates.forEach { update ->
            val date = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
                .format(update.date)
            sb.append("• $date: ${update.title}\n")
        }
        
        return sb.toString()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvCases.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyState.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvCases.visibility = if (show) View.GONE else View.VISIBLE
    }
}
