package com.example.catapult

import android.app.Application
import com.example.catapult.database.CatapultDatabase

class CatapultApp : Application() {

    override fun onCreate() {
        super.onCreate()
        CatapultDatabase.initDatabase(this)
    }
}