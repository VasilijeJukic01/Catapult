package com.example.catapult.model.catalog.list

import com.example.catapult.model.catalog.Breed

data class BreedListState (
    val fetching: Boolean = false,
    val filter: String = "",
    val breeds: List<Breed> = emptyList(),
    val currentBreeds: List<Breed> = emptyList(),
    val error: BreedListError? = null
) {
    sealed class BreedListError {
        data class BreedListUpdateFailed(val cause: Throwable? = null) : BreedListError()
    }
}