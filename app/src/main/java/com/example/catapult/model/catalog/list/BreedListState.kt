package com.example.catapult.model.catalog.list

import com.example.catapult.model.catalog.ViewBreed

data class BreedListState (
    val fetching: Boolean = false,
    val filter: String = "",
    val viewBreeds: List<ViewBreed> = emptyList(),
    val currentViewBreeds: List<ViewBreed> = emptyList(),
    val error: BreedListError? = null
) {
    sealed class BreedListError {
        data class BreedListUpdateFailed(val cause: Throwable? = null) : BreedListError()
    }
}