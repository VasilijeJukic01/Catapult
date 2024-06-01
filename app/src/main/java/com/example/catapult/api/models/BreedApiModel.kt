package com.example.catapult.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedApiModel(
    val id: String,
    val name: String = "",
    @SerialName("alt_names")
    val altNames: String = "/",
    val description: String = "",
    val temperament: String = "/",
    val origin: String = "/",
    val weight: Weight = Weight("", ""),
    @SerialName("life_span")
    val lifeSpan: String = "/",
    val adaptability: Int = 1,
    @SerialName("affection_level")
    val affectionLevel: Int = 1,
    @SerialName("energy_level")
    val energyLevel: Int = 1,
    val intelligence: Int = 1,
    @SerialName("stranger_friendly")
    val strangerFriendly: Int = 1,
    val rare: Int = 1,
    @SerialName("wikipedia_url")
    val wikipediaUrl: String = "",
    val image: Image = Image("", -1, -1, "")
)

@Serializable
data class Weight(
    val imperial: String = "",
    val metric: String = ""
)

@Serializable
data class Image(
    val id: String,
    val width: Int = -1,
    val height: Int = -1,
    val url: String = ""
)