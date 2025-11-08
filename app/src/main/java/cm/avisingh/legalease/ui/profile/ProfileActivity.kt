package cm.avisingh.legalease.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cm.avisingh.legalease.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Profile"
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
