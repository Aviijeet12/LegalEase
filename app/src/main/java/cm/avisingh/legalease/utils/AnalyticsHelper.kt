package cm.avisingh.legalease.utils

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.analytics.ktx.logEvent

class AnalyticsHelper(private val sharedPrefManager: SharedPrefManager) {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    // Track screen views
    fun trackScreenView(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "LegalEase")
            param("user_role", sharedPrefManager.getUserRole())
        }
        Log.d("Analytics", "Screen viewed: $screenName")
    }

    // Track case-related events
    fun logCaseCreated(caseId: String, caseType: String) {
        firebaseAnalytics.logEvent("case_created") {
            param("case_id", caseId)
            param("case_type", caseType)
            param("user_role", sharedPrefManager.getUserRole())
        }
        Log.d("Analytics", "Case created: $caseId, Type: $caseType")
    }

    fun logCaseViewed(caseId: String) {
        firebaseAnalytics.logEvent("case_viewed") {
            param("case_id", caseId)
            param("user_role", sharedPrefManager.getUserRole())
        }
    }

    // Track document events
    fun logDocumentUploaded(documentType: String, fileSize: Long, caseId: String? = null) {
        firebaseAnalytics.logEvent("document_uploaded") {
            param("document_type", documentType)
            param("file_size", fileSize)
            param("user_role", sharedPrefManager.getUserRole())
            if (caseId != null) {
                param("case_id", caseId)
            }
        }
        Log.d("Analytics", "Document uploaded: $documentType, Size: $fileSize")
    }

    // Track chat events
    fun logChatMessageSent(conversationId: String, messageLength: Int) {
        firebaseAnalytics.logEvent("chat_message_sent") {
            param("conversation_id", conversationId)
            param("message_length", messageLength.toLong())
            param("user_role", sharedPrefManager.getUserRole())
        }
    }

    fun logChatSessionStarted(conversationId: String, participantRole: String) {
        firebaseAnalytics.logEvent("chat_session_started") {
            param("conversation_id", conversationId)
            param("participant_role", participantRole)
            param("user_role", sharedPrefManager.getUserRole())
        }
    }

    // Track authentication events
    fun logUserLogin(method: String = "email") {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param("login_method", method)
            param("user_role", sharedPrefManager.getUserRole())
        }
        Log.d("Analytics", "User logged in: $method")
    }

    fun logUserSignup(role: String) {
        firebaseAnalytics.logEvent("user_signup") {
            param("signup_role", role)
        }
        Log.d("Analytics", "User signed up: $role")
    }

    // Track feature usage
    fun logFeatureUsed(featureName: String, duration: Long? = null) {
        firebaseAnalytics.logEvent("feature_used") {
            param("feature_name", featureName)
            param("user_role", sharedPrefManager.getUserRole())
            if (duration != null) {
                param("usage_duration_ms", duration)
            }
        }
    }

    fun trackEvent(eventName: String, parameters: Map<String, String> = emptyMap()) {
        firebaseAnalytics.logEvent(eventName) {
            parameters.forEach { (key, value) ->
                param(key, value)
            }
            param("user_role", sharedPrefManager.getUserRole())
        }
    }

    // Track errors (non-fatal)
    fun logError(errorType: String, errorMessage: String, feature: String? = null) {
        firebaseAnalytics.logEvent("app_error") {
            param("error_type", errorType)
            param("error_message", errorMessage)
            param("user_role", sharedPrefManager.getUserRole())
            if (feature != null) {
                param("feature", feature)
            }
        }
        Log.e("Analytics", "Error logged: $errorType - $errorMessage")
    }
    fun trackScreen(screenName: String) {
        // Add your tracking logic here
        Log.d("AnalyticsHelper", "Screen tracked: $screenName")
    }

    // Set user properties for segmentation
    fun setUserProperties() {
        firebaseAnalytics.setUserProperty("user_role", sharedPrefManager.getUserRole())
        firebaseAnalytics.setUserProperty("user_id", sharedPrefManager.getUserId())
    }
}