package cm.avisingh.legalease.di

import android.content.Context
import cm.avisingh.legalease.data.NotificationPreferences
import cm.avisingh.legalease.data.repository.NotificationRepository
import cm.avisingh.legalease.security.EncryptionManager
import cm.avisingh.legalease.security.SecurityManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    
    @Provides
    @Singleton
    fun provideNotificationPreferences(
        @ApplicationContext context: Context
    ): NotificationPreferences {
        return NotificationPreferences(context)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationDao: NotificationDao,
        encryptionManager: EncryptionManager,
        securityManager: SecurityManager,
        notificationPreferences: NotificationPreferences,
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): NotificationRepository {
        return NotificationRepository(
            notificationDao,
            encryptionManager,
            securityManager,
            notificationPreferences,
            firestore,
            auth,
            context
        )
    }
}