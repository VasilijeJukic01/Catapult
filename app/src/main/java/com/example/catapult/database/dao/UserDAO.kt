package com.example.catapult.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: User)

    // One time call
    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    // Whenever the state changes, this will be called
    @Query("SELECT * FROM User")
    fun observeAll(): Flow<List<User>>

}