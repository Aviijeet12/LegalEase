package com.nyayasetu.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.nyayasetu.app.ListActivity
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ListActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(ListActivity::class.java)

    @Test
    fun testRecyclerViewExists() {
        val activity = activityRule.activity
        val recyclerView = activity.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewList)
        assertNotNull(recyclerView)
    }
}
