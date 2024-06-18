package com.example.catapult.repository

import com.example.catapult.api.LeaderboardApi
import com.example.catapult.api.models.LeaderboardApiModel
import com.example.catapult.database.AppDatabase
import com.example.catapult.database.entities.LeaderboardData
import com.example.catapult.debug.ErrorTracker
import com.example.catapult.model.mappers.asLeaderboardApiModel
import com.example.catapult.model.mappers.asLeaderboardDbModel
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase,
    private val errorTracker: ErrorTracker
) {

    // Core
    suspend fun fetchLeaderboard() {
        val leaderboardCategory: MutableList<LeaderboardApiModel> = mutableListOf()
        val listSizes: MutableList<Int> = mutableListOf()

        for (categoryId in 1..3) {
            val categoryLeaderboard = leaderboardApi.getLeaderboard(categoryId = categoryId)
            leaderboardCategory.addAll(categoryLeaderboard)
            listSizes.add(categoryLeaderboard.size)
        }

        database.leaderboardDao().insertAll(leaderboardCategory.asLeaderboardDbModel(listSizes))
    }

    suspend fun submitQuizResultAPI(leaderboardData: LeaderboardData) {
        val response = leaderboardApi.postLeaderboard(leaderboardData.asLeaderboardApiModel())
        errorTracker.logText("LeaderboardRepository", response.toString())
    }

    fun submitQuizResultDB(leaderboardData: LeaderboardData) {
        database.leaderboardDao().insertResult(leaderboardData)
    }

    // Getters
    fun getLeaderboardData(categoryId: Int) = database.leaderboardDao().getAllLeaderboardDataCategory(categoryId)

    fun getTotalSubmittedGamesForUser(nickname: String): Int {
        return database.leaderboardDao().countTotalSubmittedGamesForUser(nickname)
    }

    fun getBestGlobalPositionForUser(nickname: String): Triple<Int, Int, Int> {
        val positions = (1..3).map { categoryId ->
            database.leaderboardDao().getBestGlobalPositionForUser(nickname, categoryId) ?: -1
        }
        return Triple(positions[0], positions[1], positions[2])
    }

    fun getBestResultForUser(nickname: String): Triple<Float, Float, Float> {
        val results = (1..3).map { categoryId ->
            database.leaderboardDao().getBestResultForUser(nickname, categoryId)
        }
        return Triple(results[0], results[1], results[2])
    }

    fun getQuizHistoryForUser(nickname: String): Map<Int, List<LeaderboardData>> {
        return (1..3).associateWith { categoryId ->
            database.leaderboardDao().getQuizHistoryForUser(nickname, categoryId)
        }
    }


}