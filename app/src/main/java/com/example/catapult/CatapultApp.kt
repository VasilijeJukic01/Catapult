package com.example.catapult

import android.app.Application
import com.example.catapult.database.CatapultDatabase

class CatapultApp : Application() {

    // Global application context
    override fun onCreate() {
        super.onCreate()
        CatapultDatabase.initDatabase(this)
    }
}