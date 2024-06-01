package com.example.catapult.model.catalog.grid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.model.mappers.asViewBreedImage
import com.example.catapult.repo.BreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BreedGridViewModel (
    private val breedId: String,
    private val photoRepository: BreedRepository = BreedRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(BreedGridContract.BreedGridUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedGridContract.BreedGridUiState.() -> BreedGridContract.BreedGridUiState) = _state.update(reducer)

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val images = withContext(Dispatchers.IO) {
                    photoRepository.getAllImagesForBreed(breedId = breedId)
                }
                setState { copy(images = images.map { it.asViewBreedImage() }) }
            } catch (error: Exception) {
                Log.e("BreedGridViewModel", "Failed to fetch images for breed $breedId, error: ${error.message}")
            }
            setState { copy(loading = false) }
        }
    }

}
