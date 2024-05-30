package com.example.catapult.model.mappers

import com.example.catapult.database.entities.Breed
import com.example.catapult.model.catalog.ViewBreed
import com.example.catapult.model.catalog.Characteristics

fun Breed.asViewBreed(): ViewBreed {
    return ViewBreed(
        id = this.id,
        name = this.name,
        altNames = this.altNames.split(","),
        description = this.description,
        temperament = this.temperament.split(","),
        origin = this.origin,
        weight = this.weight,
        lifeSpan = this.lifeSpan,
        rare = this.rare,
        characteristics = Characteristics(
            adaptability = this.adaptability,
            affectionLevel = this.affectionLevel,
            energyLevel = this.energyLevel,
            intelligence = this.intelligence,
            strangerFriendly = this.strangerFriendly
        ),
        wikipediaUrl = this.wikipediaUrl,
        imageUrl = this.imageUrl
    )
}