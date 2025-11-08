package cm.avisingh.legalease.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cm.avisingh.legalease.databinding.BottomSheetNotificationOptionsBinding
import cm.avisingh.legalease.notifications.InAppNotification
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

        val notification = arguments?.getParcelable<InAppNotification>(ARG_NOTIFICATION)
            ?: return dismiss()

        setupUI(notification)
    }

    private fun setupUI(notification: InAppNotification) {
        binding.apply {
            // Show/hide mark as read option based on notification status
            markAsReadButton.visibility = if (notification.isRead) {
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

        fun show(
            fragmentManager: FragmentManager,
            notification: InAppNotification,
            onMarkAsRead: () -> Unit = {},
            onDelete: () -> Unit = {}
        ) {
            val fragment = NotificationOptionsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTIFICATION, notification)
                }
                this.onMarkAsRead = onMarkAsRead
                this.onDelete = onDelete
            }
            fragment.show(fragmentManager, TAG)
        }
    }
}