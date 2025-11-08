package cm.avisingh.legalease.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cm.avisingh.legalease.databinding.BottomSheetNotificationOptionsBinding
import cm.avisingh.legalease.security.InAppNotification
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.FragmentManager

class NotificationOptionsBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetNotificationOptionsBinding? = null
    private val binding get() = _binding!!

    private var onMarkAsRead: (() -> Unit)? = null
    private var onDelete: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNotificationOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Load notification from repository using ID instead of Parcelable
        val notificationId = arguments?.getString(ARG_NOTIFICATION) ?: return dismiss()
        val isRead = arguments?.getBoolean(ARG_IS_READ, false) ?: false

        setupUI(isRead)
    }

    private fun setupUI(isRead: Boolean) {
        binding.apply {
            // Show/hide mark as read option based on notification status
            markAsReadButton.visibility = if (isRead) {
                View.GONE
            } else {
                View.VISIBLE
            }

            markAsReadButton.setOnClickListener {
                onMarkAsRead?.invoke()
                dismiss()
            }

            deleteButton.setOnClickListener {
                onDelete?.invoke()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "NotificationOptionsBottomSheet"
        private const val ARG_NOTIFICATION = "notification"
        private const val ARG_IS_READ = "is_read"

        fun show(
            fragmentManager: FragmentManager,
            notification: InAppNotification,
            onMarkAsRead: () -> Unit = {},
            onDelete: () -> Unit = {}
        ) {
            val fragment = NotificationOptionsBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_NOTIFICATION, notification.id)
                    putBoolean(ARG_IS_READ, notification.readAt != null)
                }
                this.onMarkAsRead = onMarkAsRead
                this.onDelete = onDelete
            }
            fragment.show(fragmentManager, TAG)
        }
    }
}