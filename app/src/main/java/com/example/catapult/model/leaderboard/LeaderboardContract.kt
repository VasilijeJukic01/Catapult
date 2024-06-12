package com.example.catapult.model.leaderboard

interface LeaderboardContract {

    data class LeaderboardState(
        val leaderboard: List<UILeaderboardData> = emptyList(),
        val loading: Boolean = false,
        val error: String = ""
    )

    sealed class LeaderboardUiEvent {
        data class SelectCategory(val category: Int) : LeaderboardUiEvent()
    }

}