package cm.avisingh.legalease.ui.notifications

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityNotificationsBinding
import cm.avisingh.legalease.notifications.InAppNotification
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding
    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var adapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTransitions()
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSwipeToRefresh()
        observeNotifications()
    }

    private fun setupTransitions() {
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300L
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
            scrimColor = android.R.color.transparent.toLong()
        }
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Notifications"
        }
    }

    private fun setupRecyclerView() {
        adapter = NotificationsAdapter(
            onNotificationClick = { notification -> handleNotificationClick(notification) },
            onNotificationLongClick = { notification -> showNotificationOptions(notification) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = this@NotificationsActivity.adapter
            
            // Add animation for items
            itemAnimator = CustomItemAnimator().apply {
                addDuration = 200
                removeDuration = 200
                moveDuration = 200
                changeDuration = 200
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.primary)
            setOnRefreshListener {
                viewModel.refreshNotifications()
            }
        }
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            viewModel.notificationState.collectLatest { state ->
                updateUI(state)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.swipeRefreshLayout.isRefreshing = isLoading
            }
        }
    }

    private fun updateUI(state: NotificationState) {
        when (state) {
            is NotificationState.Success -> {
                showNotifications(state.notifications)
            }
            is NotificationState.Empty -> {
                showEmptyState()
            }
            is NotificationState.Error -> {
                showError(state.message)
            }
            is NotificationState.Loading -> {
                // Handle by SwipeRefreshLayout
            }
        }
    }

    private fun showNotifications(notifications: List<InAppNotification>) {
        binding.apply {
            recyclerView.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
            errorLayout.visibility = View.GONE
        }
        adapter.submitList(notifications)
    }

    private fun showEmptyState() {
        binding.apply {
            recyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
            
            // Animate empty state appearance
            emptyStateLayout.alpha = 0f
            emptyStateLayout.animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    private fun showError(message: String) {
        binding.apply {
            recyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.GONE
            errorLayout.visibility = View.VISIBLE
            errorText.text = message

            retryButton.setOnClickListener {
                viewModel.refreshNotifications()
            }
        }
    }

    private fun handleNotificationClick(notification: InAppNotification) {
        // Mark as read and navigate
        viewModel.markAsRead(notification.id)
        navigateToNotificationContent(notification)
    }

    private fun showNotificationOptions(notification: InAppNotification) {
        NotificationOptionsBottomSheet.show(
            fragmentManager = supportFragmentManager,
            notification = notification,
            onMarkAsRead = { viewModel.markAsRead(notification.id) },
            onDelete = { viewModel.deleteNotification(notification.id) }
        )
    }

    private fun navigateToNotificationContent(notification: InAppNotification) {
        // Handle deep linking based on notification type
        when (notification.type) {
            NotificationHelper.TYPE_DOCUMENT_SHARED,
            NotificationHelper.TYPE_DOCUMENT_UPDATED -> {
                notification.data["documentId"]?.let { documentId ->
                    startActivity(
                        DocumentViewerActivity.createIntent(
                            this,
                            documentId,
                            notification.title
                        )
                    )
                }
            }
            NotificationHelper.TYPE_COMMENT_ADDED -> {
                notification.data["documentId"]?.let { documentId ->
                    startActivity(
                        DocumentCommentsActivity.createIntent(
                            this,
                            documentId
                        )
                    )
                }
            }
            // Add more cases as needed
        }
    }
}