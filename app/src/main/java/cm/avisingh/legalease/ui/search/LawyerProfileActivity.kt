package cm.avisingh.legalease.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R

class LawyerProfileActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Implement lawyer profile view
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Lawyer Profile"
    }

    companion object {
        private const val EXTRA_LAWYER_ID = "lawyer_id"

        fun start(context: Context, lawyerId: String) {
            val intent = Intent(context, LawyerProfileActivity::class.java).apply {
                putExtra(EXTRA_LAWYER_ID, lawyerId)
            }
            context.startActivity(intent)
        }
    }
}
