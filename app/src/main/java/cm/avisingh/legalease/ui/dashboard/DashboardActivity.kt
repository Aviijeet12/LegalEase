package cm.avisingh.legalease.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.data.model.Document
import cm.avisingh.legalease.databinding.ActivityDashboardBinding
import cm.avisingh.legalease.ui.documents.DocumentsActivity
import cm.avisingh.legalease.ui.profile.ProfileActivity
import cm.avisingh.legalease.ui.search.SearchActivity
import cm.avisingh.legalease.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var recentDocumentsAdapter: RecentDocumentsAdapter
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupAdapters()
        setupClickListeners()
        observeViewModel()
        loadData()
    }

    private fun setupAdapters() {
        recentDocumentsAdapter = RecentDocumentsAdapter { document ->
            openDocument(document)
        }
        binding.recentDocumentsList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentDocumentsAdapter
        }

        notificationsAdapter = NotificationsAdapter(
            onNotificationClick = { notification ->
                handleNotification(notification)
            },
            onMoreClick = { notification ->
                showNotificationOptions(notification)
            }
        )
        binding.notificationsList.adapter = notificationsAdapter
    }

    private fun setupClickListeners() {
        binding.apply {
            uploadButton.setOnClickListener { startDocumentUpload() }
            scanButton.setOnClickListener { startDocumentScan() }
            searchButton.setOnClickListener { startSearch() }
            manageStorageButton.setOnClickListener { openStorageSettings() }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.userStats.collect { stats ->
                updateWelcomeCard(stats)
            }
        }

        lifecycleScope.launch {
            viewModel.recentDocuments.collect { documents ->
                recentDocumentsAdapter.submitList(documents)
            }
        }

        lifecycleScope.launch {
            viewModel.notifications.collect { notifications ->
                notificationsAdapter.submitList(notifications)
            }
        }

        lifecycleScope.launch {
            viewModel.storageStats.collect { stats ->
                updateStorageStats(stats)
            }
        }
    }

    private fun loadData() {
        viewModel.loadUserStats()
        viewModel.loadRecentDocuments()
        viewModel.loadNotifications()
        viewModel.loadStorageStats()
    }

    private fun updateWelcomeCard(stats: UserStats) {
        binding.apply {
            welcomeText.text = "Welcome back, ${stats.userName}!"
            statsText.text = "${stats.totalDocuments} documents, ${stats.sharedDocuments} shared"
        }
    }

    private fun updateStorageStats(stats: StorageStats) {
        binding.apply {
            storageProgressBar.progress = stats.usagePercentage
            storageText.text = "${stats.usedStorage} of ${stats.totalStorage} used"
        }
    }

    private fun startDocumentUpload() {
        val intent = Intent(this, DocumentsActivity::class.java).apply {
            action = DocumentsActivity.ACTION_UPLOAD
        }
        startActivity(intent)
    }

    private fun startDocumentScan() {
        // Implement document scanning
    }

    private fun startSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private fun openDocument(document: Document) {
        val intent = Intent(this, DocumentViewerActivity::class.java).apply {
            putExtra(DocumentViewerActivity.EXTRA_DOCUMENT, document)
        }
        startActivity(intent)
    }

    private fun openStorageSettings() {
        startActivity(Intent(this, SettingsActivity::class.java).apply {
            putExtra(SettingsActivity.EXTRA_SCROLL_TO, "storage_settings")
        })
    }

    private fun handleNotification(notification: Notification) {
        when (notification.type) {
            NotificationType.SHARE -> openDocument(notification.document)
            NotificationType.COMMENT -> openDocumentComments(notification.document)
            NotificationType.SYSTEM -> handleSystemNotification(notification)
        }
    }

    private fun showNotificationOptions(notification: Notification) {
        // Show notification options menu
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}