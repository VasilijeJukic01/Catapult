package com.example.catapult.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.audio.AudioManager
import com.example.catapult.datastore.UserStore
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
class NavigationViewModel @Inject constructor(
    private val store: UserStore,
    private val repository: BreedRepository,
    private val audioManager: AudioManager
): ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(NavigationContract.NavigationState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: NavigationContract.NavigationState.() -> NavigationContract.NavigationState) = stateFlow.getAndUpdate(reducer)

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isLoggedIn = store.isUserLoggedIn()
                setState { copy(isLoggedIn = isLoggedIn) }
                repository.fetchAllBreeds()
            }
        }
        audioManager.playThemeSong()
    }

}