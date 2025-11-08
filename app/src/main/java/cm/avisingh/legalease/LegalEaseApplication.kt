package cm.avisingh.legalease

import android.app.Application
import androidx.core.os.bundleOf
// import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import cm.avisingh.legalease.notifications.NotificationChannelManager
import cm.avisingh.legalease.utils.AnalyticsHelper
import cm.avisingh.legalease.utils.SharedPrefManager
// import dagger.hilt.android.HiltAndroidApp
// import javax.inject.Inject

// @HiltAndroidApp - Temporarily disabled Hilt to fix build
class LegalEaseApplication : Application(), Configuration.Provider {
    
    // @Inject
    // lateinit var workerFactory: HiltWorkerFactory

    companion object {
        lateinit var analyticsHelper: AnalyticsHelper
    }

    private lateinit var notificationChannelManager: NotificationChannelManager

    override fun onCreate() {
        super.onCreate()

        // Initialize notification channel manager manually (Hilt disabled)
        notificationChannelManager = NotificationChannelManager(this)

        // Initialize analytics helper
        val sharedPrefManager = SharedPrefManager(this)
        analyticsHelper = AnalyticsHelper(this)

        // Set user properties
        analyticsHelper.setUserProperty("user_role", sharedPrefManager.getUserRole() ?: "unknown")

        // Log app start
        analyticsHelper.logEvent("app_launched", bundleOf(
            "app_version" to BuildConfig.VERSION_NAME,
            "user_role" to sharedPrefManager.getUserRole()
        ))
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            // .setWorkerFactory(workerFactory) // Hilt worker factory disabled
            .build()
}