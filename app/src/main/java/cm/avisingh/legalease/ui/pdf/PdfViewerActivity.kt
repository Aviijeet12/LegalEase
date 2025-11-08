package cm.avisingh.legalease.ui.pdf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R
import java.io.File

/**
 * PDF Viewer Activity - Uses WebView for PDF display
 * TODO: Add proper PDF library (e.g., com.github.barteksc:android-pdf-viewer) for better experience
 */
class PdfViewerActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // TODO: Create proper layout with WebView
        
        setupToolbar()
        setupWebView()
        loadPdf()
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        intent.getStringExtra(EXTRA_TITLE)?.let {
            title = it
        }
    }

    private fun setupWebView() {
        // TODO: Initialize webView from layout
        // webView = findViewById(R.id.webView)
        // webView.settings.apply {
        //     javaScriptEnabled = true
        //     builtInZoomControls = true
        //     displayZoomControls = false
        // }
        // webView.webViewClient = WebViewClient()
    }

    private fun loadPdf() {
        intent.getStringExtra(EXTRA_FILE_PATH)?.let { filePath ->
            try {
                // TODO: Implement PDF loading
                // Option 1: Use WebView with Google Docs Viewer
                // val url = "https://docs.google.com/gview?embedded=true&url=$encodedUrl"
                // webView.loadUrl(url)
                
                // Option 2: Use file:// protocol (may not work on all devices)
                // webView.loadUrl("file://$filePath")
                
                Toast.makeText(this, "PDF viewing not yet implemented", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error loading PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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