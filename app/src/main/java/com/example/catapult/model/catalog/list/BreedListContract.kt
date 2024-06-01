package com.example.catapult.model.catalog.list

import com.example.catapult.model.catalog.UIBreed

interface BreedListContract {

    data class BreedListState (
        val fetching: Boolean = false,
        val filter: String = "",
        val UIBreeds: List<UIBreed> = emptyList(),
        val currentUIBreeds: List<UIBreed> = emptyList(),
        val error: BreedListError? = null
    ) {
        sealed class BreedListError {
            data class BreedListUpdateFailed(val cause: Throwable? = null) : BreedListError()
        }
    }

    sealed class BreedListUiEvent {
        data class SearchChanged(val text: String) : BreedListUiEvent()
    }

}