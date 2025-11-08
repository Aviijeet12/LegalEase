package cm.avisingh.legalease

import android.app.Application
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import cm.avisingh.legalease.notifications.NotificationChannelManager
import cm.avisingh.legalease.utils.AnalyticsHelper
import cm.avisingh.legalease.utils.SharedPrefManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LegalEaseApplication : Application(), Configuration.Provider {
    
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    companion object {
        lateinit var analyticsHelper: AnalyticsHelper
    }

    @Inject
    lateinit var notificationChannelManager: NotificationChannelManager

    override fun onCreate() {
        super.onCreate()

        // Initialize analytics helper
        val sharedPrefManager = SharedPrefManager(this)
        analyticsHelper = AnalyticsHelper(sharedPrefManager)

        // Set user properties
        analyticsHelper.setUserProperties()

        // Log app start
        analyticsHelper.logEvent("app_launched", bundleOf(
            "app_version" to BuildConfig.VERSION_NAME,
            "user_role" to sharedPrefManager.getUserRole()
        ))
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}