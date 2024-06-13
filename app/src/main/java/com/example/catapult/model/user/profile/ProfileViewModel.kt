package com.example.catapult.model.user.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.model.user.profile.ProfileContract.*
import com.example.catapult.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentUser: UserData = savedStateHandle.get<String>("user")?.let {
        Json.decodeFromString<UserData>(it) } ?: throw IllegalStateException("User data missing")

    // State
    private val stateFlow = MutableStateFlow(ProfileState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: ProfileState.() -> ProfileState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<ProfileUiEvent>()

    fun setEvent(event: ProfileUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
        fetchResults()
    }

    // Events
    private fun handleEvents() {
        viewModelScope.launch {
            eventsFlow
                .collect { event ->
                    handleEvent(event)
                }
        }
    }

    private fun handleEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnLeaderBoardClick -> Unit
            is ProfileUiEvent.OnMyResultsClick -> Unit
        }
    }

    // Fetch
    private fun fetchResults() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // TODO: Fetch user results
            }
        }
    }

}

