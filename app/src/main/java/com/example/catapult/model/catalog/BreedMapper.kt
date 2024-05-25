package com.example.catapult.model.catalog

import com.example.catapult.api.BreedApiModel

object BreedMapper {

    fun mapToBreed(breedApiModel: BreedApiModel): Breed {
        return Breed(
            id = breedApiModel.id,
            name = breedApiModel.name,
            altNames = breedApiModel.altNames.split(","),
            description = breedApiModel.description,
            temperament = breedApiModel.temperament.split(","),
            origin = breedApiModel.origin,
            weight = breedApiModel.weight.imperial,
            lifeSpan = breedApiModel.lifeSpan,
            rare = breedApiModel.rare,
            characteristics = Characteristics(
                adaptability = breedApiModel.adaptability,
                affectionLevel = breedApiModel.affectionLevel,
                energyLevel = breedApiModel.energyLevel,
                intelligence = breedApiModel.intelligence,
                strangerFriendly = breedApiModel.strangerFriendly
            ),
            wikipediaUrl = breedApiModel.wikipediaUrl,
            imageUrl = breedApiModel.image.url
        )
    }

}