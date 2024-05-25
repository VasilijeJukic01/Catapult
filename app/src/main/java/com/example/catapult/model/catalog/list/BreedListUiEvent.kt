package com.example.catapult.model.catalog.list

sealed class BreedListUiEvent {
    data class SearchChanged(val text: String) : BreedListUiEvent()
}