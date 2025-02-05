package com.example.catapult.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Breed::class,
        parentColumns = ["id"],
        childColumns = ["breedId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["url"], unique = true)]
)
data class BreedImage (
    @PrimaryKey
    val id: String,
    val breedId: String,
    val url: String = "",
    val width: Int = 1,
    val height: Int = 1
)
