package com.example.catapult.model.user.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.model.user.profile.ProfileContract.*
import com.example.catapult.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val currentUser: UserData =
        savedStateHandle["user"] ?: throw IllegalStateException("User data missing")

    // State
    private val stateFlow = MutableStateFlow(ProfileState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: ProfileState.() -> ProfileState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<ProfileUiEvent>()

    fun setEvent(event: ProfileUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
    }

    // Events
    @OptIn(FlowPreview::class)
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

}

