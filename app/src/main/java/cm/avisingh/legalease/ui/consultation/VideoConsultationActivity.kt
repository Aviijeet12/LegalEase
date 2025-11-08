package cm.avisingh.legalease.ui.consultation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R

class VideoConsultationActivity : AppCompatActivity() {
    
    companion object {
        const val EXTRA_CONSULTATION_ID = "consultation_id"
        
        fun createIntent(context: android.content.Context, consultationId: String? = null): android.content.Intent {
            return android.content.Intent(context, VideoConsultationActivity::class.java).apply {
                consultationId?.let { putExtra(EXTRA_CONSULTATION_ID, it) }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_consultation)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Video Consultation"
        
        val consultationId = intent.getStringExtra(EXTRA_CONSULTATION_ID)
        
        // TODO: Implement video consultation logic
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
