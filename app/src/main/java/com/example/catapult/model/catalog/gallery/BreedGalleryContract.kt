package com.example.catapult.model.catalog.gallery

import com.example.catapult.model.catalog.ViewBreedImage

interface BreedGalleryContract {
    data class BreedGalleryUiState(
        val loading: Boolean = false,
        val images: List<ViewBreedImage> = emptyList(),
        val currentIndex: Int = 0,
    )
}