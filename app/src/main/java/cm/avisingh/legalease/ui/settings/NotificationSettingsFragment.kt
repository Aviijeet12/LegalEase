package cm.avisingh.legalease.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import cm.avisingh.legalease.databinding.FragmentNotificationSettingsBinding
import cm.avisingh.legalease.security.NotificationType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationSettingsFragment : Fragment() {

    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: NotificationSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupPreferenceToggles()
        observePreferences()
    }

    private fun setupPreferenceToggles() {
        // General notification settings
        binding.switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setPushNotificationsEnabled(isChecked)
            }
        }

        binding.switchEmailNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setEmailNotificationsEnabled(isChecked)
            }
        }

        binding.switchInAppNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setInAppNotificationsEnabled(isChecked)
            }
        }

        // Notification type settings
        binding.switchCaseUpdates.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setNotificationTypeEnabled(NotificationType.CASE_UPDATE, isChecked)
            }
        }

        binding.switchDocuments.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setNotificationTypeEnabled(NotificationType.DOCUMENT_READY, isChecked)
            }
        }

        binding.switchAppointments.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setNotificationTypeEnabled(NotificationType.APPOINTMENT, isChecked)
            }
        }

        binding.switchDeadlines.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setNotificationTypeEnabled(NotificationType.DEADLINE, isChecked)
            }
        }

        binding.switchSecurityAlerts.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setNotificationTypeEnabled(NotificationType.SECURITY, isChecked)
            }
        }
    }

    private fun observePreferences() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Observe general notification settings
            viewModel.pushNotificationsEnabled.collect {
                binding.switchPushNotifications.isChecked = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.emailNotificationsEnabled.collect {
                binding.switchEmailNotifications.isChecked = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.inAppNotificationsEnabled.collect {
                binding.switchInAppNotifications.isChecked = it
            }
        }

        // Observe notification type settings
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNotificationEnabled(NotificationType.CASE_UPDATE).collect {
                binding.switchCaseUpdates.isChecked = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNotificationEnabled(NotificationType.DOCUMENT_READY).collect {
                binding.switchDocuments.isChecked = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNotificationEnabled(NotificationType.APPOINTMENT).collect {
                binding.switchAppointments.isChecked = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNotificationEnabled(NotificationType.DEADLINE).collect {
                binding.switchDeadlines.isChecked = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNotificationEnabled(NotificationType.SECURITY).collect {
                binding.switchSecurityAlerts.isChecked = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}