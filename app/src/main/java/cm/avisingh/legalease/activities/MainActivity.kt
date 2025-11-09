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

        try {
            // Initialize with safe defaults
            analyticsHelper = AnalyticsHelper(this)
            notificationHelper = NotificationHelper(this)
            caseViewModel = CaseViewModel()
            sharedPrefManager = SharedPrefManager(this)

            setupObservers()
            setupDashboard()
            setupClickListeners()

            analyticsHelper.trackScreenView("Main Dashboard")
            analyticsHelper.logFeatureUsed("Dashboard")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error initializing: ${e.message}", Toast.LENGTH_SHORT).show()
        }
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
        try {
            android.util.Log.d("MainActivity", "=== SETUP DASHBOARD STARTED ===")
            
            // First, check if the calling Intent passed a role (LoginActivity will include it)
            val intentRole = intent.getStringExtra("user_role")
            android.util.Log.d("MainActivity", "Intent extra 'user_role': '$intentRole'")
            
            if (!intentRole.isNullOrEmpty()) {
                // Persist intent role to SharedPreferences so subsequent runs/readers see it
                android.util.Log.d("MainActivity", "Saving intent role '$intentRole' to SharedPreferences")
                sharedPrefManager.setUserRole(intentRole)
            } else {
                android.util.Log.w("MainActivity", "No role passed via intent!")
            }

            val userRole = sharedPrefManager.getUserRole()
            val userName = sharedPrefManager.getUserName()

            // Debug logging
            android.util.Log.d("MainActivity", "Final User Role from SharedPrefs: '$userRole'")
            android.util.Log.d("MainActivity", "User Name: '$userName'")
            
            // Check if role is empty - redirect to login
            if (userRole.isEmpty()) {
                android.util.Log.e("MainActivity", "Role is empty! User not properly authenticated. Redirecting to login.")
                Toast.makeText(this, "Please sign in again to select your role", Toast.LENGTH_LONG).show()
                // Clear data and redirect to login
                sharedPrefManager.clearUserData()
                redirectToLogin()
                return
            }

            // Show role as toast for debugging
            Toast.makeText(this, "Role: $userRole", Toast.LENGTH_LONG).show()
            
            when (userRole.trim().lowercase()) {
                "lawyer" -> {
                    android.util.Log.d("MainActivity", "Showing Lawyer Dashboard")
                    showLawyerDashboard(userName)
                }
                "client" -> {
                    android.util.Log.d("MainActivity", "Showing Client Dashboard")
                    showClientDashboard(userName)
                }
                else -> {
                    android.util.Log.d("MainActivity", "Unknown role: '$userRole', defaulting to Client Dashboard")
                    // Default to client dashboard
                    showClientDashboard(userName)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("MainActivity", "Error in setupDashboard", e)
            // Show default client dashboard as fallback
            showClientDashboard("User")
        }
    }
    
    private fun redirectToLogin() {
        val intent = Intent(this, cm.avisingh.legalease.ui.auth.LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showLawyerDashboard(userName: String) {
        // Inflate lawyer dashboard layout
        val lawyerView = layoutInflater.inflate(cm.avisingh.legalease.R.layout.layout_lawyer_dashboard, null)
        setContentView(lawyerView)

        // Find views and set data
        val tvLawyerWelcome = lawyerView.findViewById<android.widget.TextView>(cm.avisingh.legalease.R.id.tvLawyerWelcome)
        val tvActiveCases = lawyerView.findViewById<android.widget.TextView>(cm.avisingh.legalease.R.id.tvActiveCases)
        val tvTodayHearings = lawyerView.findViewById<android.widget.TextView>(cm.avisingh.legalease.R.id.tvTodayHearings)
        val ivProfile = lawyerView.findViewById<android.widget.ImageView>(cm.avisingh.legalease.R.id.ivProfile)

        tvLawyerWelcome.text = "Welcome, ${if (userName.isNotEmpty()) userName else "Advocate"}!"
        tvActiveCases.text = "12"
        tvTodayHearings.text = "3"

        // Setup lawyer dashboard click listeners
        setupLawyerClickListeners(lawyerView, ivProfile)
    }

    private fun showClientDashboard(userName: String) {
        // Inflate client dashboard layout
        val clientView = layoutInflater.inflate(cm.avisingh.legalease.R.layout.layout_client_dashboard, null)
        setContentView(clientView)

        // Find views and set data
        val tvClientWelcome = clientView.findViewById<android.widget.TextView>(cm.avisingh.legalease.R.id.tvClientWelcome)
        val tvMyCasesCount = clientView.findViewById<android.widget.TextView>(cm.avisingh.legalease.R.id.tvMyCasesCount)
        val ivProfile = clientView.findViewById<android.widget.ImageView>(cm.avisingh.legalease.R.id.ivProfile)

        tvClientWelcome.text = "Welcome back, ${if (userName.isNotEmpty()) userName else "Client"}!"
        tvMyCasesCount.text = "5"

        // Setup client dashboard click listeners
        setupClientClickListeners(clientView, ivProfile)
    }

    private fun setupClickListeners() {
        // This method is now split into setupLawyerClickListeners and setupClientClickListeners
    }

    private fun setupLawyerClickListeners(view: android.view.View, ivProfile: android.widget.ImageView) {
        // Find lawyer dashboard cards
        val cardAddCase = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardAddCase)
        val cardViewClients = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardViewClients)
        val cardDocuments = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardDocuments)
        val cardCalendar = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardCalendar)
        val cardHearings = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardHearings)
        val cardReports = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardReports)

        cardAddCase.setOnClickListener {
            analyticsHelper.trackEvent("feature_click", mapOf("feature" to "add_case"))
            startActivity(Intent(this, LawyerCasesActivity::class.java))
        }

        cardViewClients.setOnClickListener {
            analyticsHelper.trackEvent("feature_click", mapOf("feature" to "view_clients"))
            startActivity(Intent(this, LawyerClientsActivity::class.java))
        }

        cardDocuments.setOnClickListener {
            startActivity(Intent(this, DocumentsListActivity::class.java))
        }

        cardCalendar.setOnClickListener {
            startActivity(Intent(this, AppointmentsActivity::class.java))
        }

        cardHearings.setOnClickListener {
            startActivity(Intent(this, LawyerCasesActivity::class.java))
        }

        cardReports.setOnClickListener {
            Toast.makeText(this, "Reports - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        ivProfile.setOnClickListener {
            showProfileMenu()
        }
    }

    private fun setupClientClickListeners(view: android.view.View, ivProfile: android.widget.ImageView) {
        // Find client dashboard cards
        val cardContactLawyer = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardContactLawyer)
        val cardUploadDocument = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardUploadDocument)
        val cardCaseStatus = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardCaseStatus)
        val cardDocuments = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardDocuments)
        val cardAppointments = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardAppointments)
        val cardHelp = view.findViewById<com.google.android.material.card.MaterialCardView>(cm.avisingh.legalease.R.id.cardHelp)

        cardContactLawyer.setOnClickListener {
            startActivity(Intent(this, LawyersListActivity::class.java))
        }

        cardUploadDocument.setOnClickListener {
            startActivity(Intent(this, DocumentUploadActivity::class.java))
        }

        cardCaseStatus.setOnClickListener {
            startActivity(Intent(this, CaseStatusActivity::class.java))
        }

        cardDocuments.setOnClickListener {
            startActivity(Intent(this, DocumentsListActivity::class.java))
        }

        cardAppointments.setOnClickListener {
            startActivity(Intent(this, AppointmentsActivity::class.java))
        }

        cardHelp.setOnClickListener {
            startActivity(Intent(this, HelpSupportActivity::class.java))
        }

        ivProfile.setOnClickListener {
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