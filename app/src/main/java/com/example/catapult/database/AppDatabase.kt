package com.example.catapult.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catapult.database.dao.BreedDAO
import com.example.catapult.database.dao.UserDAO
import com.example.catapult.database.entities.Breed
import com.example.catapult.database.entities.User

@Database(
    entities = [
        User::class,
        Breed::class
    ],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO

    abstract fun breedDao(): BreedDAO

}