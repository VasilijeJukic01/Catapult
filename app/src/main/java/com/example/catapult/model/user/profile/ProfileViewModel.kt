package com.example.catapult.model.user.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserStore
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
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val store: UserStore,
    private val repository: LeaderboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val nickname = savedStateHandle.get<String>("user") ?: throw IllegalStateException("user required")

    // State
    private val stateFlow = MutableStateFlow(ProfileState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: ProfileState.() -> ProfileState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<ProfileUiEvent>()

    fun setEvent(event: ProfileUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
        fetchUser()
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
    private fun fetchUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = store.getUserByNickname(nickname)
                setState { copy(currentUser = user) }
            }
        }
    }

    private fun fetchResults() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.fetchLeaderboard()

                val quizHistory = repository.getQuizHistoryForUser(nickname)
                val bestResults = repository.getBestResultForUser(nickname)
                val bestGlobalPositions = repository.getBestGlobalPositionForUser(nickname)

                setState { copy(quizHistory = quizHistory, bestResults = bestResults, bestGlobalPositions = bestGlobalPositions) }
            }
        }
    }



}

