package com.nyayasetu.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.nyayasetu.app.LoginActivity
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun testLoginButtonExists() {
        val activity = activityRule.activity
        val loginButton = activity.findViewById<android.widget.Button>(R.id.buttonLogin)
        assertNotNull(loginButton)
    }
}
