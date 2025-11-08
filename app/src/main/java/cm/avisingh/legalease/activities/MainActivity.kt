package cm.avisingh.legalease.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cm.avisingh.legalease.LegalEaseApplication
import cm.avisingh.legalease.databinding.ActivityMainBinding
import cm.avisingh.legalease.ui.auth.LoginActivity
import cm.avisingh.legalease.utils.AnalyticsHelper
import cm.avisingh.legalease.utils.NotificationHelper
import cm.avisingh.legalease.utils.SharedPrefManager
import cm.avisingh.legalease.viewmodels.CaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var caseViewModel: CaseViewModel
    private lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analyticsHelper = LegalEaseApplication.analyticsHelper
        notificationHelper = NotificationHelper(this)
        caseViewModel = CaseViewModel()
        sharedPrefManager = SharedPrefManager(this)

        setupObservers()
        setupDashboard()
        setupClickListeners()

        analyticsHelper.trackScreenView("Main Dashboard")
        analyticsHelper.logFeatureUsed("Dashboard")
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            caseViewModel.casesState.collectLatest { state ->
                when (state) {
                    is CaseViewModel.CasesState.Loading -> {
                        // Show loading
                    }
                    is CaseViewModel.CasesState.Success -> {
                        // Update UI with real cases
                        updateCasesUI(state.cases)
                    }
                    is CaseViewModel.CasesState.Error -> {
                        // Show error
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadRealData() {
        val userId = sharedPrefManager.getUserId()
        val userRole = sharedPrefManager.getUserRole()

        if (userId.isNotEmpty()) {
            caseViewModel.loadCases(userId, userRole)
        }
    }

    private fun setupDemoNotifications() {
        // Add a hidden area or menu option to test notifications
        binding.ivProfile.setOnLongClickListener {
            showDemoNotificationMenu()
            true
        }
    }

    private fun showDemoNotificationMenu() {
        val options = arrayOf("Case Update", "Hearing Reminder", "New Message", "Cancel")

        android.app.AlertDialog.Builder(this)
            .setTitle("Test Notifications")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> notificationHelper.simulateCaseUpdate()
                    1 -> notificationHelper.simulateHearingReminder()
                    2 -> notificationHelper.simulateNewMessage()
                }
            }
            .show()
    }

    // Function removed - duplicate onCreate

    private fun setupDashboard() {
        val userRole = sharedPrefManager.getUserRole()
        val userName = sharedPrefManager.getUserName()
        val userEmail = sharedPrefManager.getUserEmail()

        when (userRole) {
            "lawyer" -> showLawyerDashboard(userName, userEmail)
            "client" -> showClientDashboard(userName, userEmail)
            else -> {
                // Fallback to login if no role
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun showLawyerDashboard(userName: String, userEmail: String) {
        binding.layoutLawyerDashboard.visibility = android.view.View.VISIBLE
        binding.layoutClientDashboard.visibility = android.view.View.GONE

        binding.tvLawyerWelcome.text = "Welcome, ${if (userName.isNotEmpty()) userName else "Advocate"}!"
        binding.tvToolbarTitle.text = "Lawyer Dashboard"

        // Set mock data
        binding.tvActiveCases.text = "12"
        binding.tvTodayHearings.text = "3"
    }

    private fun showClientDashboard(userName: String, userEmail: String) {
        binding.layoutLawyerDashboard.visibility = android.view.View.GONE
        binding.layoutClientDashboard.visibility = android.view.View.VISIBLE

        binding.tvClientWelcome.text = "Welcome back, ${if (userName.isNotEmpty()) userName else "Client"}!"
        binding.tvToolbarTitle.text = "My LegalEase"

        // Set mock data
        binding.tvMyCasesCount.text = "5"
    }

    private fun setupClickListeners() {
        // Lawyer dashboard actions
        binding.cardAddCase.setOnClickListener {
            analyticsHelper.trackEvent("feature_click", mapOf("feature" to "add_case"))
            analyticsHelper.logFeatureUsed("Add Case Button")
            Toast.makeText(this, "Add New Case - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        // Test crash reporting (remove later)
        binding.ivProfile.setOnLongClickListener {
            // Test crash - remove in production!
            throw RuntimeException("Test Crash for Crashlytics")
            true
        }

        binding.cardViewClients.setOnClickListener {
            analyticsHelper.trackEvent("feature_click", mapOf("feature" to "view_clients"))
            analyticsHelper.logFeatureUsed("View Clients Button")
            Toast.makeText(this, "View Clients - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        binding.cardDocuments.setOnClickListener {
            Toast.makeText(this, "Documents - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        binding.cardCalendar.setOnClickListener {
            Toast.makeText(this, "Calendar - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        // Client dashboard actions
        binding.cardContactLawyer.setOnClickListener {
            Toast.makeText(this, "Contact Lawyer - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        binding.cardUploadDocument.setOnClickListener {
            val intent = Intent(this, DocumentUploadActivity::class.java)
            startActivity(intent)
        }

        binding.cardCaseStatus.setOnClickListener {
            Toast.makeText(this, "Case Status - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        // Profile icon
        binding.ivProfile.setOnClickListener {
            showProfileMenu()
        }
    }

    private fun showProfileMenu() {
        val items = arrayOf("Profile", "Settings", "Logout")

        android.app.AlertDialog.Builder(this)
            .setTitle("Account")
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Profile - Coming Soon!", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "Settings - Coming Soon!", Toast.LENGTH_SHORT).show()
                    2 -> logout()
                }
            }
            .show()
    }

    private fun updateCasesUI(cases: List<Any>) {
        // TODO: Update RecyclerView with actual cases data
        // For now, just log or show toast
    }

    private fun logout() {
        sharedPrefManager.clearUserData()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}