package cm.avisingh.legalease.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cm.avisingh.legalease.R
import cm.avisingh.legalease.notifications.NotificationPreferences
import kotlinx.coroutines.launch

class NotificationSettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: NotificationSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        
        viewModel = ViewModelProvider(this)[NotificationSettingsViewModel::class.java]
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notification Settings"
        
        // TODO: Implement UI when layout is complete
        observePreferences()
    }

    private fun setupPreferencesUI() {
        // TODO: Setup switches when layout is complete
        /*binding.apply {
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
        }*/
    }

    private fun observePreferences() {
        lifecycleScope.launch {
            viewModel.notificationPreferences.collect { preferences ->
                updateSwitchesFromPreferences(preferences)
            }
        }
    }

    private fun updateSwitchesFromPreferences(preferences: NotificationPreferences) {
        // TODO: Update UI when layout is complete
    }

    private fun updateSwitchesState(enabled: Boolean) {
        // TODO: Update switches state when layout is complete
    }

    private fun updatePreferences(update: (NotificationPreferences) -> NotificationPreferences) {
        lifecycleScope.launch {
            viewModel.updatePreferences(update)
        }
    }
}