package com.example.catapult.model.catalog

data class UIBreed(
    val id: String,
    val name: String,
    val altNames: List<String>,
    val description: String,
    val temperament: List<String>,
    val origin: String,
    val weight: String,
    val lifeSpan: String,
    val rare: Int,
    val characteristics: Characteristics,
    val wikipediaUrl: String,
    val imageUrl: String
) {

}