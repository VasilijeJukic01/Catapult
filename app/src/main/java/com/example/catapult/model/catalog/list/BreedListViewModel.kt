package com.example.catapult.model.catalog.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.repository.BreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.catapult.model.catalog.list.BreedListContract.BreedListState
import com.example.catapult.model.catalog.list.BreedListContract.BreedListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Hot Flow - Type of flow that is active independently of the presence of collectors.

SharedFlow - Hot Flow that emits values to its collectors.
StateFlow - Hot Flow that represents a state. It emits the current state to new collectors.
 */

@HiltViewModel
class BreedListViewModel @Inject constructor (
    private val repository: BreedRepository
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(BreedListState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: BreedListState.() -> BreedListState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<BreedListUiEvent>()

    fun setEvent(event: BreedListUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
        fetchBreeds()
    }

    // Events
    @OptIn(FlowPreview::class)
    private fun handleEvents() {
        viewModelScope.launch {
            eventsFlow
                .debounce(300)
                .collect { event ->
                    handleEvent(event)
                }
        }
    }

    private fun handleEvent(event: BreedListUiEvent) {
        when (event) {
            // Search
            is BreedListUiEvent.SearchChanged -> {
                setState { copy(filter = event.text) }
                handleSearch()
                println("Search changed: ${event.text}")
            }
        }
    }

    // Event handlers
    private fun handleSearch() {
        val filter = stateFlow.value.filter
        val breeds = stateFlow.value.UIBreeds
        val filtered = breeds.filter { it.name.contains(filter, ignoreCase = true) }
        setState { copy(currentUIBreeds = filtered) }
    }

    // Fetching
    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    val allBreeds = repository.allBreeds()
                    withContext(Dispatchers.Main) {
                        setState { copy(
                            UIBreeds = allBreeds,
                            currentUIBreeds = allBreeds
                        ) }
                    }
                    println("Fetched breeds")
                }
            } catch (error: Exception) {
                setState { copy(error = BreedListState.BreedListError.BreedListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
                println("Fetch breeds done")
            }
        }
    }

}