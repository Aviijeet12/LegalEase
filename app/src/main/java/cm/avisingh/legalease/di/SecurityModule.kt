package cm.avisingh.legalease.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import cm.avisingh.legalease.security.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    
    @Provides
    @Singleton
    fun provideEncryptionManager(
        @ApplicationContext context: Context
    ): EncryptionManager {
        return EncryptionManager(context)
    }

    @Provides
    @Singleton
    fun provideSecurityManager(
        @ApplicationContext context: Context,
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): SecurityManager {
        return SecurityManager(context, firestore, auth)
    }

    @Provides
    @Singleton
    fun provideTwoFactorAuthManager(
        @ApplicationContext context: Context,
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        securityManager: SecurityManager
    ): TwoFactorAuthManager {
        return TwoFactorAuthManager(context, auth, firestore, securityManager)
    }

    @Provides
    @Singleton
    fun provideSecureDocumentManager(
        @ApplicationContext context: Context,
        storage: FirebaseStorage,
        auth: FirebaseAuth,
        encryptionManager: EncryptionManager,
        securityManager: SecurityManager
    ): SecureDocumentManager {
        return SecureDocumentManager(context, storage, auth, encryptionManager, securityManager)
    }
}