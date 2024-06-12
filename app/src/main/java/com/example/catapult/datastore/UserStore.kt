package com.example.catapult.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(
    private val persistence: DataStore<UserData>
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val userData = persistence.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { persistence.data.first() },
        )

    suspend fun updateUserData(newUserData: UserData) {
        persistence.updateData { newUserData }
        Log.d("AuthStore", "Data updated: $newUserData")
    }

    suspend fun isUserLoggedIn(): Boolean {
        val userData = persistence.data.first()
        return userData.firstName.isNotEmpty() &&
                userData.lastName.isNotEmpty() &&
                userData.nickname.isNotEmpty() &&
                userData.email.isNotEmpty()
    }

}