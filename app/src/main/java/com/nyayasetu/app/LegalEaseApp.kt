package com.nyayasetu.app

import android.app.Application

class LegalEaseApp : Application() {

    companion object {
        lateinit var instance: LegalEaseApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize any global components here
        initializeApp()
    }

    private fun initializeApp() {
        // Initialize preference helper, network client, etc.
    }
}