package com.example.catapult.model.catalog.details

import com.example.catapult.model.catalog.UIBreed

data class BreedDetailsState (
    val fetching: Boolean = false,
    val breedId: String = "",
    val data: UIBreed? = null,
    val error: BreedDetailsError? = null
) {
    sealed class BreedDetailsError {
        data class BreedDetailsUpdateFailed(val cause: Throwable? = null) : BreedDetailsError()
    }
}