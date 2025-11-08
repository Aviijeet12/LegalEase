package cm.avisingh.legalease.ui.documents

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import cm.avisingh.legalease.data.model.Document
import cm.avisingh.legalease.data.sharing.DocumentSharingManager
import cm.avisingh.legalease.databinding.DialogShareDocumentBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ShareDocumentDialog : DialogFragment() {
    private var _binding: DialogShareDocumentBinding? = null
    private val binding get() = _binding!!
    private lateinit var document: Document
    private lateinit var sharingManager: DocumentSharingManager
    private lateinit var sharedUsersAdapter: SharedUsersAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_LegalEase_Dialog_FullScreen)
        document = arguments?.getParcelable(ARG_DOCUMENT)!!
        sharingManager = DocumentSharingManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogShareDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        loadSharedUsers()
    }

    private fun setupUI() {
        binding.apply {
            // Set document name
            documentNameText.text = document.name

            // Setup tabs
            viewPager.adapter = SharePagerAdapter()
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "People"
                    1 -> "Link"
                    else -> ""
                }
            }.attach()

            // Setup shared users list
            sharedUsersAdapter = SharedUsersAdapter { email ->
                removeUser(email)
            }
            sharedWithList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = sharedUsersAdapter
            }

            // Setup email input
            addButton.setOnClickListener {
                val email = emailEditText.text.toString()
                if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    shareWithUser(email)
                    emailEditText.text?.clear()
                } else {
                    emailInputLayout.error = "Enter a valid email address"
                }
            }

            // Setup link generation
            generateLinkButton.setOnClickListener {
                generateLink()
            }

            linkInputLayout.setEndIconOnClickListener {
                copyLinkToClipboard()
            }

            // Setup expiration settings
            expirationSwitch.setOnCheckedChangeListener { _, isChecked ->
                expirationInputLayout.isEnabled = isChecked
            }
        }
    }

    private fun loadSharedUsers() {
        lifecycleScope.launch {
            val users = sharingManager.getSharedUsers(document.id)
            sharedUsersAdapter.submitList(users)
        }
    }

    private fun shareWithUser(email: String) {
        lifecycleScope.launch {
            try {
                val success = sharingManager.shareDocument(document, listOf(email))
                if (success.isNotEmpty()) {
                    loadSharedUsers()
                    Toast.makeText(context, "Document shared with $email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to share document", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeUser(email: String) {
        lifecycleScope.launch {
            try {
                sharingManager.removeSharing(document, email)
                loadSharedUsers()
                Toast.makeText(context, "Removed sharing with $email", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateLink() {
        lifecycleScope.launch {
            try {
                binding.generateLinkButton.isEnabled = false
                val expireDays = if (binding.expirationSwitch.isChecked) {
                    binding.expirationEditText.text.toString().toIntOrNull() ?: 7
                } else {
                    0 // No expiration
                }
                
                val link = sharingManager.createSharingLink(document, expireDays)
                binding.linkEditText.setText(link.toString())
                binding.generateLinkButton.isEnabled = true
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                binding.generateLinkButton.isEnabled = true
            }
        }
    }

    private fun copyLinkToClipboard() {
        val link = binding.linkEditText.text.toString()
        if (link.isNotEmpty()) {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Sharing Link", link)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class SharePagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = 2
        override fun createFragment(position: Int) = when (position) {
            0 -> PeopleShareFragment()
            1 -> LinkShareFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        private const val ARG_DOCUMENT = "document"

        fun newInstance(document: Document): ShareDocumentDialog {
            return ShareDocumentDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DOCUMENT, document)
                }
            }
        }
    }
}