package cm.avisingh.legalease.ui.lawyer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R

class LawyerProfileActivity : AppCompatActivity() {
    
    companion object {
        const val EXTRA_LAWYER_ID = "lawyer_id"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lawyer_profile)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Lawyer Profile"
        
        val lawyerId = intent.getStringExtra(EXTRA_LAWYER_ID)
        
        // TODO: Implement lawyer profile logic
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
