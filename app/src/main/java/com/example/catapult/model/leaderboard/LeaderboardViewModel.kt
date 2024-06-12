package com.example.catapult.model.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.catapult.model.leaderboard.LeaderboardContract.*
import com.example.catapult.model.mappers.asLeaderboardUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(LeaderboardState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<LeaderboardUiEvent>()

    fun setEvent(event: LeaderboardUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }


    init {
        handleEvents()
        fetchLeaderboardData()
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

    private suspend fun handleEvent(event: LeaderboardUiEvent) {
        when (event) {
            // Select Category
            is LeaderboardUiEvent.SelectCategory -> {
                withContext(Dispatchers.IO) {
                    val leaderboardData: List<UILeaderboardData> = repository.getLeaderboardData(event.category).asLeaderboardUiModel()
                    setState { copy(leaderboard = leaderboardData) }
                }
            }
        }
    }

    // Fetch
    private fun fetchLeaderboardData() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchLeaderboard()
                    setState { copy(loading = false) }
                }
            } catch (e: Exception) {
                Log.e("LeaderboardViewModel", "fetchLeaderboardData: Error", e)
                setState { copy(loading = false, error = e.message ?: "An error occurred") }
            }
        }
    }

}