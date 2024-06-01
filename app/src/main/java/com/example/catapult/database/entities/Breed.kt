package com.example.catapult.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Breed (
    @PrimaryKey
    val id: String,
    val name: String,
    val altNames: String,
    val description: String,
    val temperament: String,
    val origin: String,
    val weight: String,
    val lifeSpan: String,
    val adaptability: Int,
    val affectionLevel: Int,
    val energyLevel: Int,
    val intelligence: Int,
    val strangerFriendly: Int,
    val rare: Int,
    val wikipediaUrl: String,
    val imageUrl: String
) {
    init {
        require(adaptability in 1..5) { "Adaptability must be between 1 and 5" }
        require(affectionLevel in 1..5) { "AffectionLevel must be between 1 and 5" }
        require(energyLevel in 1..5) { "EnergyLevel must be between 1 and 5" }
        require(intelligence in 1..5) { "Intelligence must be between 1 and 5" }
        require(strangerFriendly in 1..5) { "StrangerFriendly must be between 1 and 5" }
    }
}