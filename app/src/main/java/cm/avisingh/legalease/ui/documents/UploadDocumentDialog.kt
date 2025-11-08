package cm.avisingh.legalease.ui.documents

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.DialogUploadDocumentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UploadDocumentDialog : DialogFragment() {
    private var _binding: DialogUploadDocumentBinding? = null
    private val binding get() = _binding!!
    private lateinit var fileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Create ThemeOverlay_LegalEase_Dialog theme
        setStyle(STYLE_NORMAL, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUploadDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fileUri = arguments?.getParcelable(ARG_FILE_URI)!!

        setupCategoryDropdown()
        setupFilePreview()
        setupButtons()
    }

    private fun setupCategoryDropdown() {
        val categories = listOf("Property", "Legal", "Financial", "Personal", "Other")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            categories
        )
        binding.categoryDropdown.setAdapter(adapter)
    }

    private fun setupFilePreview() {
        // Get file metadata
        context?.contentResolver?.query(
            fileUri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            cursor.moveToFirst()
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
            
            val fileName = cursor.getString(nameIndex)
            val fileSize = cursor.getLong(sizeIndex)

            binding.apply {
                fileNameText.text = fileName
                fileSizeText.text = formatFileSize(fileSize)
                nameEditText.setText(fileName)
            }
        }
    }

    private fun setupButtons() {
        binding.apply {
            cancelButton.setOnClickListener { dismiss() }
            uploadButton.setOnClickListener { startUpload() }
        }
    }

    private fun startUpload() {
        // Validate inputs
        val category = binding.categoryDropdown.text.toString()
        val name = binding.nameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()

        if (category.isEmpty()) {
            binding.categoryInputLayout.error = "Please select a category"
            return
        }

        if (name.isEmpty()) {
            binding.nameInputLayout.error = "Please enter a name"
            return
        }

        // Show upload progress
        binding.apply {
            uploadProgressLayout.visibility = View.VISIBLE
            uploadButton.isEnabled = false
            cancelButton.isEnabled = false

            // Simulate upload progress
            uploadProgressBar.setProgressCompat(0, true)
            var progress = 0
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            val runnable = object : Runnable {
                override fun run() {
                    progress += 5
                    uploadProgressBar.setProgressCompat(progress, true)
                    uploadStatusText.text = "Uploading... $progress%"

                    if (progress < 100) {
                        handler.postDelayed(this, 100)
                    } else {
                        onUploadComplete()
                    }
                }
            }
            handler.post(runnable)
        }
    }

    private fun onUploadComplete() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Upload Complete")
            .setMessage("Your document has been uploaded successfully.")
            .setPositiveButton("OK") { _, _ -> dismiss() }
            .show()
    }

    private fun formatFileSize(size: Long): String {
        val kb = size / 1024.0
        return when {
            kb < 1024 -> String.format("%.1f KB", kb)
            else -> String.format("%.1f MB", kb / 1024)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FILE_URI = "file_uri"

        fun show(fragmentManager: FragmentManager, fileUri: Uri) {
            UploadDocumentDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FILE_URI, fileUri)
                }
            }.show(fragmentManager, "UPLOAD_DOCUMENT")
        }
    }
}