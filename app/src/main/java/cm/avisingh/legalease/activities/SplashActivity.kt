package cm.avisingh.legalease.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import cm.avisingh.legalease.activities.MainActivity
import cm.avisingh.legalease.R
import cm.avisingh.legalease.utils.SharedPrefManager
import cm.avisingh.legalease.utils.AnalyticsHelper

class SplashActivity : AppCompatActivity() {

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        analyticsHelper.trackScreen(this::class.java.simpleName)

        sharedPrefManager = SharedPrefManager(this)

        // Delay for 2 seconds then check user state
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserState()
        }, 2000)
    }

    private fun checkUserState() {
        val intent = when {
            sharedPrefManager.isFirstLaunch() -> {
                Intent(this, OnboardingActivity::class.java)
            }
            sharedPrefManager.isLoggedIn() -> {
                Intent(this, MainActivity::class.java)
            }
            else -> {
                Intent(this, LoginActivity::class.java)
            }
        }

        startActivity(intent)
        finish()
    }
}