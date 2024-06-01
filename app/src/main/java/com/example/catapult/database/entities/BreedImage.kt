package com.example.catapult.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Breed::class,
    parentColumns = ["id"],
    childColumns = ["breedId"],
    onDelete = ForeignKey.CASCADE
)])
data class BreedImage (
    @PrimaryKey
    val id: String,
    val breedId: String,
    val url: String,
    val width: Int,
    val height: Int
)