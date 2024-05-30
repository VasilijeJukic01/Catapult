package com.example.catapult.model.mappers

import com.example.catapult.api.BreedApiModel
import com.example.catapult.database.entities.Breed

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