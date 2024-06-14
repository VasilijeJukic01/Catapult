package com.example.catapult.model.catalog

data class UIBreed(
    val id: String = "",
    val name: String = "",
    val altNames: List<String> = emptyList(),
    val description: String = "",
    val temperament: List<String> = emptyList(),
    val origin: String = "",
    val weight: String = "",
    val lifeSpan: String = "",
    val rare: Int = 0,
    val characteristics: Characteristics = Characteristics(),
    val wikipediaUrl: String = "",
    val imageUrl: String = ""
) {

}