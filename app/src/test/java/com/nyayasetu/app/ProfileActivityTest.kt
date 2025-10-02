package com.nyayasetu.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.nyayasetu.app.ProfileActivity
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(ProfileActivity::class.java)

    @Test
    fun testSaveButtonExists() {
        val activity = activityRule.activity
        val saveButton = activity.findViewById<android.widget.Button>(R.id.buttonSave)
        assertNotNull(saveButton)
    }
}
