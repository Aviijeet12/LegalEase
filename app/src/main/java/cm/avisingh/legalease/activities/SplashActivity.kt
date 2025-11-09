package cm.avisingh.legalease.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import cm.avisingh.legalease.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d("SplashActivity", "onCreate started")

        // Delay for 2 seconds then navigate to onboarding
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                Log.d("SplashActivity", "Navigating to OnboardingActivity")
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
                Log.d("SplashActivity", "Navigation complete")
            } catch (e: Exception) {
                Log.e("SplashActivity", "Error navigating", e)
                e.printStackTrace()
            }
        }, 2000)
    }
}