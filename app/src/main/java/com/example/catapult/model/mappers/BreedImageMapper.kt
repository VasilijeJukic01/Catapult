package com.example.catapult.model.mappers

import com.example.catapult.api.models.BreedImageApiModel
import com.example.catapult.database.entities.BreedImage
import com.example.catapult.model.catalog.UIBreedImage

fun BreedImageApiModel.asBreedImageDbModel(): BreedImage {
    return BreedImage(
        id = this.id,
        breedId = this.breedId,
        url = this.url,
        height = this.height,
        width = this.width
    )
}

fun BreedImage.asViewBreedImage(): UIBreedImage {
    return UIBreedImage(
        id = this.id,
        breedId = this.breedId,
        url = this.url,
        height = this.height,
        width = this.width
    )
}