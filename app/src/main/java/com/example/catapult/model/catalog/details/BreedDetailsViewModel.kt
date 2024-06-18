package com.example.catapult.model.catalog.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository
) : ViewModel() {

    private val breedId = savedStateHandle.get<String>("id") ?: throw IllegalStateException("breedId required")

    // State
    private val stateFlow = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) = stateFlow.getAndUpdate(reducer)

    init {
        fetchBreed()
    }

    // Fetching
    private fun fetchBreed() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    val breed = repository.fetchBreedDetails(breedId)
                    setState { copy(data = breed) }
                }
            } catch (e: Exception) {
                setState {
                    copy(error = BreedDetailsState.BreedDetailsError.BreedDetailsUpdateFailed(e))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

}