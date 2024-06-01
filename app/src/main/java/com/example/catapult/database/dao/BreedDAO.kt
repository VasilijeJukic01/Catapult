package com.example.catapult.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.database.entities.Breed
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Breed>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(breed: Breed)

    @Query("SELECT * FROM Breed")
    fun getAll(): List<Breed>

    @Query("SELECT * FROM Breed")
    fun observeAll(): Flow<List<Breed>>

    @Query("SELECT * FROM Breed WHERE id = :breedId")
    fun getBreedById(breedId: String): Breed?

}