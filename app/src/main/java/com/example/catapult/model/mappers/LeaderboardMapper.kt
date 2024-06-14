package com.example.catapult.model.mappers

import com.example.catapult.api.models.LeaderboardApiModel
import com.example.catapult.database.entities.LeaderboardData
import com.example.catapult.model.leaderboard.UILeaderboardData

fun List<LeaderboardApiModel>.asLeaderboardDbModel(listSizes: MutableList<Int>): List<LeaderboardData> {
    val startIndexes = listSizes.scan(0) { acc, size -> acc + size }

    return this.mapIndexed { index, apiModel ->
        val categoryStartIndex = startIndexes[apiModel.category - 1]
        val positionInCategory = index - categoryStartIndex + 1

        LeaderboardData(
            id = index + 1,
            nickname = apiModel.nickname,
            position = positionInCategory,
            result = apiModel.result,
            category = apiModel.category,
            createdAt = apiModel.createdAt,
            submitted = 1
        )
    }
}

fun List<LeaderboardData>.asLeaderboardUiModel(): List<UILeaderboardData> {
    return this.map { dbModel ->
        UILeaderboardData(
            nickname = dbModel.nickname,
            position = dbModel.position,
            result = dbModel.result,
            totalGamesSubmitted = 0
        )
    }
}

fun LeaderboardData.asLeaderboardApiModel(): LeaderboardApiModel {
    return LeaderboardApiModel(
        category = this.category,
        nickname = this.nickname,
        result = this.result
    )
}