package com.example.catapult.api

import kotlinx.serialization.Serializable

@Serializable
data class BreedImageApiModel(
    val id: String,
    var breedId: String = "",
    val url: String = "",
    val width: Int = 1,
    val height: Int = 1
)