package com.example.catapult.model.leaderboard

data class UILeaderboardData(
    val position: Int = 0,
    val nickname: String = "",
    val result: Float = 0.0f,
    val totalGamesSubmitted: Int = 0
)