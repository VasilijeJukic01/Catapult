package com.example.catapult.api

import com.example.catapult.api.models.LeaderboardApiModel
import com.example.catapult.api.models.PostLeaderboardResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {

    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") categoryId: Int,
    ): List<LeaderboardApiModel>

    @POST("leaderboard")
    suspend fun postLeaderboard(
        @Body leaderboard: LeaderboardApiModel
    ): PostLeaderboardResponse

}