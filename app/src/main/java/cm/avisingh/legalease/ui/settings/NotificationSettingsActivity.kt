package cm.avisingh.legalease.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cm.avisingh.legalease.databinding.ActivityNotificationSettingsBinding
import cm.avisingh.legalease.notifications.NotificationPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationSettingsBinding
    private val viewModel: NotificationSettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupPreferencesUI()
        observePreferences()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notification Settings"
    }

    private fun setupPreferencesUI() {
        binding.apply {
            // Listen to switch changes
            pushNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
                updatePreferences { it.copy(pushEnabled = isChecked) }
                updateSwitchesState(isChecked)
            }

            emailNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
                updatePreferences { it.copy(emailEnabled = isChecked) }
            }

            documentSharingSwitch.setOnCheckedChangeListener { _, isChecked ->
                updatePreferences { it.copy(documentSharing = isChecked) }
            }

            documentUpdatesSwitch.setOnCheckedChangeListener { _, isChecked ->
                updatePreferences { it.copy(documentUpdates = isChecked) }
            }

            commentsSwitch.setOnCheckedChangeListener { _, isChecked ->
                updatePreferences { it.copy(comments = isChecked) }
            }

            systemUpdatesSwitch.setOnCheckedChangeListener { _, isChecked ->
                updatePreferences { it.copy(systemUpdates = isChecked) }
            }
        }
    }

    private fun observePreferences() {
        lifecycleScope.launch {
            viewModel.notificationPreferences.collect { preferences ->
                updateSwitchesFromPreferences(preferences)
            }
        }
    }

    private fun updateSwitchesFromPreferences(preferences: NotificationPreferences) {
        binding.apply {
            pushNotificationsSwitch.isChecked = preferences.pushEnabled
            emailNotificationsSwitch.isChecked = preferences.emailEnabled
            documentSharingSwitch.isChecked = preferences.documentSharing
            documentUpdatesSwitch.isChecked = preferences.documentUpdates
            commentsSwitch.isChecked = preferences.comments
            systemUpdatesSwitch.isChecked = preferences.systemUpdates

            updateSwitchesState(preferences.pushEnabled)
        }
    }

    private fun updateSwitchesState(enabled: Boolean) {
        binding.apply {
            documentSharingSwitch.isEnabled = enabled
            documentUpdatesSwitch.isEnabled = enabled
            commentsSwitch.isEnabled = enabled
            systemUpdatesSwitch.isEnabled = enabled
        }
    }

    private fun updatePreferences(update: (NotificationPreferences) -> NotificationPreferences) {
        lifecycleScope.launch {
            viewModel.updatePreferences(update)
        }
    }
}