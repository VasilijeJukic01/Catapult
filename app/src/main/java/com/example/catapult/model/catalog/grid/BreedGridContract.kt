package com.example.catapult.model.catalog.grid

import com.example.catapult.model.catalog.UIBreedImage

interface BreedGridContract {

    data class BreedGridUiState(
        val loading: Boolean = false,
        val images: List<UIBreedImage> = emptyList(),
    )

}