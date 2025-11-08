package cm.avisingh.legalease.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R

class DocumentCommentsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // TODO: Create proper layout
        
        val documentId = intent.getStringExtra(EXTRA_DOCUMENT_ID)
        // TODO: Implement document comments
    }
    
    companion object {
        const val EXTRA_DOCUMENT_ID = "document_id"
        
        fun createIntent(context: Context, documentId: String): Intent {
            return Intent(context, DocumentCommentsActivity::class.java).apply {
                putExtra(EXTRA_DOCUMENT_ID, documentId)
            }
        }
    }
}
