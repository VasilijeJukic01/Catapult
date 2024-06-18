package com.example.catapult.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(
    private val persistence: DataStore<List<UserData>>
) {

    suspend fun addUserData(newUserData: UserData) {
        persistence.updateData { currentUsers ->
            val updatedUsers = currentUsers
                .map { user -> user.copy(active = 0) }
                .toMutableList()
            updatedUsers.add(newUserData)
            updatedUsers
        }
    }

    suspend fun updateUserData(updatedUserData: UserData) {
        persistence.updateData { currentUsers ->
            currentUsers.map { user ->
                if (user.active == 1) updatedUserData else user
            }
        }
    }

    suspend fun setUserData(newUserData: UserData) {
        persistence.updateData { listOf(newUserData) }
    }

    suspend fun switchUser(activeUser: UserData) {
        persistence.updateData { currentUsers ->
            currentUsers.map { user ->
                user.copy(active = if (user == activeUser) 1 else 0)
            }
        }
    }

    suspend fun deleteUser(userToDelete: UserData) {
        persistence.updateData { currentUsers ->
            currentUsers.filter { it.nickname != userToDelete.nickname }
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        val users = persistence.data.first()
        return users.any { user ->
            user.firstName.isNotEmpty() &&
                    user.lastName.isNotEmpty() &&
                    user.nickname.isNotEmpty() &&
                    user.email.isNotEmpty()
        }
    }

    fun getAllUsers(): Flow<List<UserData>> {
        return persistence.data
    }

    suspend fun getActiveUser(): UserData {
        persistence.data.firstOrNull()?.forEach { user ->
            if (user.active == 1) {
                return user
            }
        }
        return null!!
    }

    suspend fun getUserByNickname(nickname: String): UserData {
        persistence.data.firstOrNull()?.forEach { user ->
            if (user.nickname == nickname.substring(1, nickname.length-1)) {
                return user
            }
        }
        return null!!
    }
}

