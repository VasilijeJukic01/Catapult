package com.example.catapult.model.catalog.details

import com.example.catapult.model.catalog.Breed

data class BreedDetailsState (
    val fetching: Boolean = false,
    val breedId: String = "",
    val data: Breed? = null,
    val error: BreedDetailsError? = null
) {
    sealed class BreedDetailsError {
        data class BreedDetailsUpdateFailed(val cause: Throwable? = null) : BreedDetailsError()
    }
}