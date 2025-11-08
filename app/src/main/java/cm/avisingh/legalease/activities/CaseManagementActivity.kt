package cm.avisingh.legalease.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.LegalEaseApplication
import cm.avisingh.legalease.adapters.CaseAdapter
import cm.avisingh.legalease.databinding.ActivityCaseManagementBinding
import cm.avisingh.legalease.models.Case
import cm.avisingh.legalease.utils.SharedPrefManager
import java.util.*
import cm.avisingh.legalease.R
import cm.avisingh.legalease.utils.AnalyticsHelper

class CaseManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseManagementBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var caseAdapter: CaseAdapter
    private lateinit var analyticsHelper: AnalyticsHelper

    private val allCases = mutableListOf<Case>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager(this)

        // Initialize analytics helper first
        analyticsHelper = LegalEaseApplication.analyticsHelper
        analyticsHelper.trackScreenView("Case Management")

        setupUI()
        setupClickListeners()
        loadMockCases()
    }

    private fun setupUI() {
        // Setup toolbar based on role - FIXED: Use string resource
        if (sharedPrefManager.getUserRole() == "client") {
            binding.tvToolbarTitle.text = getString(R.string.my_cases)
            binding.ivAddCase.visibility = View.GONE
        }

        // Setup RecyclerView
        caseAdapter = CaseAdapter(allCases) { case ->
            openCaseDetails(case)
        }
        binding.rvCases.apply {
            layoutManager = LinearLayoutManager(this@CaseManagementActivity)
            adapter = caseAdapter
        }

        // Setup tabs
        binding.tabLayoutCases.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> caseAdapter.filterCases("All")
                    1 -> caseAdapter.filterCases("Active")
                    2 -> caseAdapter.filterCases("Pending")
                    3 -> caseAdapter.filterCases("Closed")
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivAddCase.setOnClickListener {
            createNewCase()
        }

        binding.btnCreateFirstCase.setOnClickListener {
            createNewCase()
        }
    }

    private fun loadMockCases() {
        val mockCases = listOf(
            Case(
                id = "1",
                caseNumber = "CIV-2024-001",
                title = "Property Boundary Dispute",
                description = "Dispute over property boundaries between adjacent landowners",
                clientName = "Robert Miller",
                clientEmail = "robert@email.com",
                lawyerId = "lawyer_1",
                caseType = "Civil Law",
                status = "Active",
                priority = "High",
                courtName = "District Court",
                nextHearingDate = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000), // 7 days from now
                caseFiledDate = Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000) // 30 days ago - ADDED
            ),
            Case(
                id = "2",
                caseNumber = "CR-2024-015",
                title = "State vs Davis - Fraud Case",
                description = "Criminal case involving alleged financial fraud",
                clientName = "Michael Davis",
                clientEmail = "davis@email.com",
                lawyerId = "lawyer_1",
                caseType = "Criminal Law",
                status = "Pending",
                priority = "High",
                courtName = "High Court",
                nextHearingDate = Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000), // 14 days
                caseFiledDate = Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000) // 15 days ago - ADDED
            ),
            Case(
                id = "3",
                caseNumber = "CORP-2024-003",
                title = "Corporate Merger Agreement",
                description = "Legal documentation for corporate merger between TechCorp and Innovate Ltd",
                clientName = "Sarah Johnson",
                clientEmail = "sarah@techcorp.com",
                lawyerId = "lawyer_1",
                caseType = "Corporate Law",
                status = "Active",
                priority = "Medium",
                courtName = "Corporate Tribunal",
                nextHearingDate = null,
                caseFiledDate = Date(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000) // 10 days ago - ADDED
            ),
            Case(
                id = "4",
                caseNumber = "FAM-2024-008",
                title = "Child Custody Case",
                description = "Child custody and visitation rights dispute",
                clientName = "Jennifer Wilson",
                clientEmail = "jennifer@email.com",
                lawyerId = "lawyer_1",
                caseType = "Family Law",
                status = "Closed",
                priority = "Medium",
                courtName = "Family Court",
                nextHearingDate = null,
                caseFiledDate = Date(System.currentTimeMillis() - 90 * 24 * 60 * 60 * 1000) // 90 days ago - ADDED
            )
        )

        allCases.clear()
        allCases.addAll(mockCases)
        caseAdapter.updateCases(allCases)

        updateStats()
        checkEmptyState()
    }

    private fun updateStats() {
        val activeCount = allCases.count { it.status == "Active" }
        val pendingCount = allCases.count { it.status == "Pending" }
        val closedCount = allCases.count { it.status == "Closed" }

        binding.tvActiveCasesCount.text = activeCount.toString()
        binding.tvPendingCasesCount.text = pendingCount.toString()
        binding.tvClosedCasesCount.text = closedCount.toString()
    }

    private fun checkEmptyState() {
        if (allCases.isEmpty()) {
            binding.rvCases.visibility = View.GONE
            binding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvCases.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        }
    }

    private fun openCaseDetails(case: Case) {
        val intent = Intent(this, CaseDetailActivity::class.java).apply {
            putExtra("case_id", case.id)
            putExtra("case_number", case.caseNumber)
        }
        analyticsHelper.trackEvent("case_viewed", mapOf("case_id" to case.id))
        analyticsHelper.trackEvent("feature_used", mapOf("feature" to "Case Details View"))
        startActivity(intent)
    }

    private fun createNewCase() {
        Toast.makeText(this, getString(R.string.create_case_coming_soon), Toast.LENGTH_SHORT).show()
        analyticsHelper.trackEvent("case_created", mapOf("case_type" to "Civil"))
    }
}