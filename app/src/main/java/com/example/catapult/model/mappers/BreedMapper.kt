package com.example.catapult.model.mappers

import com.example.catapult.api.models.BreedApiModel
import com.example.catapult.database.entities.Breed
import com.example.catapult.model.catalog.Characteristics
import com.example.catapult.model.catalog.UIBreed

fun BreedApiModel.asBreedDbModel(): Breed {
    return Breed(
        id = this.id,
        name = this.name,
        altNames = this.altNames,
        description = this.description,
        temperament = this.temperament,
        origin = this.origin,
        weight = this.weight.metric,
        lifeSpan = this.lifeSpan,
        adaptability = this.adaptability,
        affectionLevel = this.affectionLevel,
        energyLevel = this.energyLevel,
        intelligence = this.intelligence,
        strangerFriendly = this.strangerFriendly,
        rare = this.rare,
        wikipediaUrl = this.wikipediaUrl,
        imageUrl = this.image.url
    )
}

fun Breed.asViewBreed(): UIBreed {
    return UIBreed(
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