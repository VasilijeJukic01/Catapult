package com.example.catapult.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catapult.database.dao.BreedDAO
import com.example.catapult.database.dao.BreedImageDAO
import com.example.catapult.database.entities.Breed
import com.example.catapult.database.entities.BreedImage

@Database(
    entities = [
        Breed::class,
        BreedImage::class
    ],
    version = 9,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedDao(): BreedDAO

    abstract fun breedImageDao(): BreedImageDAO

}