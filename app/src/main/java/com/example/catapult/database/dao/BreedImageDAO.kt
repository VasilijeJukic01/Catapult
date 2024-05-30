package com.example.catapult.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.database.entities.BreedImage

@Dao
interface BreedImageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<BreedImage>)

    @Query("SELECT * FROM BreedImage WHERE breedId = :breedId")
    fun getAllImagesForBreed(breedId: String): List<BreedImage>
}