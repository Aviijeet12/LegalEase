package cm.avisingh.legalease.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cm.avisingh.legalease.adapters.CaseUpdateAdapter
import cm.avisingh.legalease.adapters.TimelineAdapter
import cm.avisingh.legalease.databinding.ActivityCaseDetailBinding
import cm.avisingh.legalease.models.CaseUpdate
import cm.avisingh.legalease.models.TimelineEvent
import java.util.*

class CaseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaseDetailBinding
    private lateinit var caseUpdateAdapter: CaseUpdateAdapter
    private lateinit var timelineAdapter: TimelineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize adapters
        caseUpdateAdapter = CaseUpdateAdapter()
        timelineAdapter = TimelineAdapter()

        // Setup RecyclerViews - FIXED: Use correct IDs from XML
        binding.rvCaseUpdates.adapter = caseUpdateAdapter
        binding.rvCaseTimeline.adapter = timelineAdapter // Changed from rvTimeline to rvCaseTimeline

        // Track screen
        trackScreen("CaseDetail")

        // Load data
        loadCaseUpdates()
        loadTimelineEvents()

        // Setup click listeners for buttons
        setupClickListeners()
    }

    private fun trackScreen(screenName: String) {
        // Simple implementation - you can add Firebase Analytics later
        println("Screen tracked: $screenName")
    }

    private fun setupClickListeners() {
        binding.btnChat.setOnClickListener {
            // Handle chat button click
        }

        binding.btnDocuments.setOnClickListener {
            // Handle documents button click
        }
    }

    private fun loadCaseUpdates() {
        val updates = listOf(
            CaseUpdate(
                id = "1",
                title = "Hearing Scheduled",
                description = "Next hearing scheduled for final arguments",
                date = Date(),
                type = "HEARING",
                caseId = "case_123"
            ),
            CaseUpdate(
                id = "2",
                title = "Document Filed",
                description = "Submitted evidence documents",
                date = Date(System.currentTimeMillis() - 86400000), // Yesterday
                type = "DOCUMENT",
                caseId = "case_123"
            ),
            CaseUpdate(
                id = "3",
                title = "Witness List Submitted",
                description = "List of witnesses submitted to the court",
                date = Date(System.currentTimeMillis() - 172800000), // 2 days ago
                type = "DOCUMENT",
                caseId = "case_123"
            )
        )
        caseUpdateAdapter.updateUpdates(updates)
    }

    private fun loadTimelineEvents() {
        val events = listOf(
            TimelineEvent(
                id = "1",
                title = "Case Filed",
                description = "Case filed in district court",
                date = Date(System.currentTimeMillis() - 2592000000L), // 30 days ago
                eventType = "FILED",
                caseId = "case_123"
            ),
            TimelineEvent(
                id = "2",
                title = "First Hearing",
                description = "First hearing completed - both parties present",
                date = Date(System.currentTimeMillis() - 1728000000L), // 20 days ago
                eventType = "HEARING",
                caseId = "case_123"
            ),
            TimelineEvent(
                id = "3",
                title = "Evidence Submission",
                description = "Both parties submitted initial evidence",
                date = Date(System.currentTimeMillis() - 864000000L), // 10 days ago
                eventType = "DOCUMENT",
                caseId = "case_123"
            ),
            TimelineEvent(
                id = "4",
                title = "Site Inspection",
                description = "Court ordered site inspection completed",
                date = Date(System.currentTimeMillis() - 432000000L), // 5 days ago
                eventType = "INSPECTION",
                caseId = "case_123"
            )
        )
        timelineAdapter.updateEvents(events)
    }
}