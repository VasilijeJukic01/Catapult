package com.example.catapult.model.user.profile

import com.example.catapult.database.entities.LeaderboardData
import com.example.catapult.datastore.UserData

interface ProfileContract {

    data class ProfileState(
        val currentUser: UserData = UserData(),
        val quizResults: List<LeaderboardData> = emptyList(),
        val bestResults: Triple<Int, Int, Int> = Triple(-1, -1, -1),
        val bestGlobalPositions: Triple<Int, Int, Int> = Triple(-1, -1, -1),
        val isError: Boolean = false,
        val errorMessage: String = ""
    )

    sealed class ProfileUiEvent {
        data class OnLeaderBoardClick(val currentUser: UserData) : ProfileUiEvent()
        data class OnMyResultsClick(val currentUser: UserData) : ProfileUiEvent()
    }

}