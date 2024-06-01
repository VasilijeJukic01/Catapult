package com.example.catapult.model.mappers

import com.example.catapult.api.BreedImageApiModel
import com.example.catapult.database.entities.BreedImage
import com.example.catapult.model.catalog.ViewBreedImage

fun BreedImageApiModel.asBreedImageDbModel(): BreedImage {
    return BreedImage(
        id = this.id,
        breedId = this.breedId,
        url = this.url,
        height = this.height,
        width = this.width
    )
}

fun BreedImage.asViewBreedImage(): ViewBreedImage {
    return ViewBreedImage(
        id = this.id,
        breedId = this.breedId,
        url = this.url,
        height = this.height,
        width = this.width
    )
}