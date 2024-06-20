package com.example.catapult

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Generates the Hilt component for the application
@HiltAndroidApp
class CatapultApp : Application() {

    // Global application context
    override fun onCreate() {
        super.onCreate()
    }
}