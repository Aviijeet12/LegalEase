package cm.avisingh.legalease.ui.pdf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import cm.avisingh.legalease.databinding.ActivityPdfViewerBinding
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File

class PdfViewerActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {
    private lateinit var binding: ActivityPdfViewerBinding
    private var totalPages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupPdfView()
        setupControls()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        
        intent.getStringExtra(EXTRA_TITLE)?.let {
            binding.toolbar.title = it
        }
    }

    private fun setupPdfView() {
        binding.progressIndicator.visibility = View.VISIBLE

        intent.getStringExtra(EXTRA_FILE_PATH)?.let { filePath ->
            val file = File(filePath)
            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                file
            )

            binding.pdfView.apply {
                fromUri(uri)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true)
                    .scrollHandle(DefaultScrollHandle(this@PdfViewerActivity))
                    .spacing(10)
                    .onPageChange(this@PdfViewerActivity)
                    .onLoad(this@PdfViewerActivity)
                    .load()
            }
        }
    }

    private fun setupControls() {
        // Previous page button
        binding.previousButton.setOnClickListener {
            if (binding.pdfView.currentPage > 0) {
                binding.pdfView.jumpTo(binding.pdfView.currentPage - 1)
            }
        }

        // Next page button
        binding.nextButton.setOnClickListener {
            if (binding.pdfView.currentPage < totalPages - 1) {
                binding.pdfView.jumpTo(binding.pdfView.currentPage + 1)
            }
        }

        updatePageControls(0)
    }

    private fun updatePageControls(pageNumber: Int) {
        binding.previousButton.isEnabled = pageNumber > 0
        binding.nextButton.isEnabled = pageNumber < totalPages - 1
        binding.pageNumberText.text = "Page ${pageNumber + 1} of $totalPages"
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        updatePageControls(page)
    }

    override fun loadComplete(nbPages: Int) {
        totalPages = nbPages
        binding.progressIndicator.visibility = View.GONE
        updatePageControls(binding.pdfView.currentPage)
    }

    companion object {
        private const val EXTRA_FILE_PATH = "file_path"
        private const val EXTRA_TITLE = "title"

        fun createIntent(context: Activity, filePath: String, title: String): Intent {
            return Intent(context, PdfViewerActivity::class.java).apply {
                putExtra(EXTRA_FILE_PATH, filePath)
                putExtra(EXTRA_TITLE, title)
            }
        }
    }
}