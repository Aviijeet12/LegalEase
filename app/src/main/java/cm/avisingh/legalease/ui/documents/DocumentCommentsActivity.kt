package cm.avisingh.legalease.ui.documents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R

class DocumentCommentsActivity : AppCompatActivity() {
    
    companion object {
        const val EXTRA_DOCUMENT_ID = "document_id"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_comments)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Comments"
        
        val documentId = intent.getStringExtra(EXTRA_DOCUMENT_ID)
        
        // TODO: Implement comments logic
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
