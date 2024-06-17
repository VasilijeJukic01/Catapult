package com.example.catapult.model.user.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.datastore.UserStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.catapult.model.user.edit.EditUserContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@HiltViewModel
class EditUserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val store: UserStore,
) : ViewModel() {

    private val nickname = savedStateHandle.get<String>("user") ?: throw IllegalStateException("user required")

    // State
    private val stateFlow = MutableStateFlow(EditUserState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: EditUserState.() -> EditUserState) = stateFlow.update(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<EditUserUiEvent>()

    fun setEvent(event: EditUserUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
        fetchUser()
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

    private suspend fun handleEvent(event: EditUserUiEvent) {
        when (event) {
            // Edit Profile
            is EditUserUiEvent.OnSubmitClick -> {
                val user = UserData(event.avatar, event.firstName, event.lastName, event.nickname, event.email,1)
                store.updateUserData(user)
            }
        }
    }

    // Fetch
    private fun fetchUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = store.getUserByNickname(nickname)
                setState { copy(user = user) }
            }
        }
    }

}