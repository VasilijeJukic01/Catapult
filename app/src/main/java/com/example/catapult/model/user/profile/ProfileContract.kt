package com.example.catapult.model.user.profile

import com.example.catapult.datastore.UserData

interface ProfileContract {

    data class ProfileState(
        val currentUser: UserData = UserData(),
        val isError: Boolean = false,
        val errorMessage: String = ""
    )

    sealed class ProfileUiEvent {
        data class OnLeaderBoardClick(val currentUser: UserData) : ProfileUiEvent()
        data class OnMyResultsClick(val currentUser: UserData) : ProfileUiEvent()
    }

}