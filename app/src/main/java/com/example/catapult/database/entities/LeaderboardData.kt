package com.example.catapult.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LeaderboardData (
    @PrimaryKey
    val id: Int = 0,
    val nickname: String,
    val position: Int,
    val result: Float,
    val category: Int,
    val createdAt: Long,
    val submitted: Int
)