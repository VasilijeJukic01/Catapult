package com.example.catapult.repository

import com.example.catapult.api.LeaderboardApi
import com.example.catapult.api.models.LeaderboardApiModel
import com.example.catapult.database.AppDatabase
import com.example.catapult.model.mappers.asLeaderboardDbModel
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase
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

    // Getters
    fun getLeaderboardData(categoryId: Int) = database.leaderboardDao().getAllLeaderboardDataCategory(categoryId)

}