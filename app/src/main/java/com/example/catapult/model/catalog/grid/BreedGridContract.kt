package com.example.catapult.model.catalog.grid

import com.example.catapult.model.catalog.ViewBreedImage

interface BreedGridContract {
    data class BreedGridUiState(
        val loading: Boolean = false,
        val images: List<ViewBreedImage> = emptyList(),
    )
}