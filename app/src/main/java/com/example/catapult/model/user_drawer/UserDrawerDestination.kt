package com.example.catapult.model.user_drawer

import com.example.catapult.datastore.UserData

sealed class UserDrawerDestination {

    data class Profile(val user: UserData) : UserDrawerDestination()
    data class EditProfile(val user: UserData) : UserDrawerDestination()
    data object AddProfile : UserDrawerDestination()
    data object Leaderboard : UserDrawerDestination()

}