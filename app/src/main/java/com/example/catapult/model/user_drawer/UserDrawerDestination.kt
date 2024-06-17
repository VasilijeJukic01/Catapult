package com.example.catapult.model.user_drawer

sealed class UserDrawerDestination {

    data class Profile(val user: String) : UserDrawerDestination()
    data class EditProfile(val user: String) : UserDrawerDestination()
    data object AddProfile : UserDrawerDestination()
    data object Leaderboard : UserDrawerDestination()

}