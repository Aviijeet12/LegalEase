package cm.avisingh.legalease.ui.documents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R

class DocumentViewerActivity : AppCompatActivity() {
    
    companion object {
        const val EXTRA_DOCUMENT_ID = "document_id"
        const val EXTRA_DOCUMENT_URI = "document_uri"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_viewer)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Document Viewer"
        
        val documentId = intent.getStringExtra(EXTRA_DOCUMENT_ID)
        val documentUri = intent.getStringExtra(EXTRA_DOCUMENT_URI)
        
        // TODO: Implement document viewing logic
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
