package com.nyayasetu.app

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LegalEasePrefs", Context.MODE_PRIVATE)

    // User preferences
    fun saveUserType(userType: String) {
        sharedPreferences.edit().putString("user_type", userType).apply()
    }

    fun getUserType(): String? {
        return sharedPreferences.getString("user_type", null)
    }

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString("user_id", userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }

    fun saveUserEmail(email: String) {
        sharedPreferences.edit().putString("user_email", email).apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }

    // Login state
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    // Language preference
    fun saveLanguage(language: String) {
        sharedPreferences.edit().putString("app_language", language).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString("app_language", "en") ?: "en"
    }

    // Clear all preferences (logout)
    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}