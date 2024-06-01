package com.example.catapult.model.catalog.gallery

import com.example.catapult.model.catalog.UIBreedImage

interface BreedGalleryContract {

    data class BreedGalleryUiState(
        val loading: Boolean = false,
        val images: List<UIBreedImage> = emptyList(),
        val currentIndex: Int = 0,
    )

}