package com.example.catapult.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.database.entities.LeaderboardData

@Dao
interface LeaderboardDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<LeaderboardData>)

    @Query("SELECT * FROM LeaderboardData WHERE category = :categoryId")
    fun getAllLeaderboardDataCategory(categoryId: Int): List<LeaderboardData>

    @Query("SELECT COUNT(*) FROM LeaderboardData WHERE nickname = :nickname AND submitted = 1")
    fun countTotalSubmittedGamesForUser(nickname: String): Int

    @Query("SELECT * FROM LeaderboardData WHERE nickname = :nickname AND category = :categoryId ORDER BY createdAt DESC")
    fun getQuizHistoryForUser(nickname: String, categoryId: Int): List<LeaderboardData>

    @Query("SELECT MAX(result) FROM LeaderboardData WHERE nickname = :nickname AND category = :categoryId")
    fun getBestResultForUser(nickname: String, categoryId: Int): Float

    @Query("SELECT MIN(position) FROM LeaderboardData WHERE nickname = :nickname AND category = :categoryId")
    fun getBestGlobalPositionForUser(nickname: String, categoryId: Int): Int?

}