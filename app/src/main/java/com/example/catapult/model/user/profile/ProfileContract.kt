package com.example.catapult.model.user.profile

import com.example.catapult.database.entities.LeaderboardData
import com.example.catapult.datastore.UserData

interface ProfileContract {

    data class ProfileState(
        val currentUser: UserData = UserData(),
        val quizHistory: Map<Int, List<LeaderboardData>> = emptyMap(),//history
        val bestResults: Triple<Float, Float, Float> = Triple(-1.0f, -1.0f, -1.0f),
        val bestGlobalPositions: Triple<Int, Int, Int> = Triple(-1, -1, -1),
        val isError: Boolean = false,
        val errorMessage: String = ""
    )

    sealed class ProfileUiEvent {
        data class OnLeaderBoardClick(val currentUser: UserData) : ProfileUiEvent()
        data class OnMyResultsClick(val currentUser: UserData) : ProfileUiEvent()
    }

}