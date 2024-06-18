package com.example.catapult.navigation

interface NavigationContract {

    data class NavigationState(
        val isLoggedIn: Boolean = false,
    )

}