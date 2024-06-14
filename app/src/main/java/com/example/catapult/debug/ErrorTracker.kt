package com.example.catapult.debug

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ErrorTracker {

    fun logError(tag: String, message: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        Log.e(tag, "Error at $timestamp: $message")
    }

    fun throwError(tag: String, message: String): Nothing {
        logError(tag, message)
        throw Exception(message)
    }

}