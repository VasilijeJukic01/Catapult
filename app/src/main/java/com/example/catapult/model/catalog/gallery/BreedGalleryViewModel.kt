package com.example.catapult.model.catalog.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.model.mappers.asViewBreedImage
import com.example.catapult.repository.BreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BreedGalleryViewModel (
    private val breedId: String,
    private val currentImage: String,
    private val breedRepository: BreedRepository = BreedRepository,
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(BreedGalleryContract.BreedGalleryUiState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: BreedGalleryContract.BreedGalleryUiState.() -> BreedGalleryContract.BreedGalleryUiState) =
        stateFlow.update(reducer)

    init {
        fetchImages()
    }

    // Fetching
    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val images = withContext(Dispatchers.IO) {
                    breedRepository.allImagesForBreed(breedId = breedId)
                }
                val imagesView = images.map { it.asViewBreedImage() }
                val currentIndex = imagesView.indexOfFirst { it.id == currentImage }

                setState { copy(images = imagesView, currentIndex = currentIndex) }
            } catch (error: Exception) {
                Log.e("BreedGalleryViewModel", "Failed to fetch images for breed $breedId, error: ${error.message}")
            }
            setState { copy(loading = false) }
        }
    }
}