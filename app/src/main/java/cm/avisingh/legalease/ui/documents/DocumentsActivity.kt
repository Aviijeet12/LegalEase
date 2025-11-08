package cm.avisingh.legalease.ui.documents

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityDocumentsBinding
import cm.avisingh.legalease.ui.search.SearchActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator

class DocumentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocumentsBinding
    private lateinit var categoryAdapter: DocumentCategoryAdapter
    private var totalStorage = 0L
    private var documentCount = 0

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                showUploadDialog(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
        setupUploadButton()
        loadDocumentStats()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupViewPager() {
        categoryAdapter = DocumentCategoryAdapter(this)
        binding.viewPager.apply {
            adapter = categoryAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

            // Disable swipe when selecting text
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    binding.viewPager.isUserInputEnabled = state == ViewPager2.SCROLL_STATE_IDLE
                }
            })
        }

        // Setup tab titles
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Property"
                2 -> "Legal"
                3 -> "Financial"
                4 -> "Personal"
                else -> "Other"
            }
        }.attach()
    }

    private fun setupUploadButton() {
        binding.uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                    "application/pdf",
                    "image/*",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                ))
            }
            filePickerLauncher.launch(intent)
        }
    }

    private fun loadDocumentStats() {
        // Will be replaced with actual API call
        binding.apply {
            totalStorageText.text = "2.5 GB"
            documentCountText.text = "48"
        }
    }

    private fun showUploadDialog(fileUri: Uri) {
        UploadDocumentDialog.show(supportFragmentManager, fileUri)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_documents, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                // Launch search
                SearchActivity.start(this)
                true
            }
            R.id.sort_name -> {
                categoryAdapter.getCurrentFragment()?.sortDocuments(SortType.NAME)
                true
            }
            R.id.sort_date -> {
                categoryAdapter.getCurrentFragment()?.sortDocuments(SortType.DATE)
                true
            }
            R.id.sort_size -> {
                categoryAdapter.getCurrentFragment()?.sortDocuments(SortType.SIZE)
                true
            }
            R.id.action_select -> {
                categoryAdapter.getCurrentFragment()?.toggleSelectionMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDeleteConfirmation(selectedCount: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Documents")
            .setMessage("Are you sure you want to delete $selectedCount selected documents?")
            .setPositiveButton("Delete") { _, _ ->
                categoryAdapter.getCurrentFragment()?.deleteSelectedDocuments()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, DocumentsActivity::class.java))
        }
    }
}