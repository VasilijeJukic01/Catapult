package com.example.catapult.model.user.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.datastore.UserStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.catapult.model.user.edit.EditUserContract.*
import kotlinx.coroutines.FlowPreview

class EditUserViewModel @Inject constructor(
    private val store: UserStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val currentUser: UserData =
        savedStateHandle["user"] ?: throw IllegalStateException("User data missing")

    // State
    private val stateFlow = MutableStateFlow(EditUserState(currentUser))
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: EditUserState.() -> EditUserState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<EditUserUiEvent.OnSubmitClick>()

    fun setEvent(event: EditUserUiEvent.OnSubmitClick) = viewModelScope.launch { eventsFlow.emit(event) }

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

    private suspend fun handleEvent(event: EditUserUiEvent) {
        when (event) {
            is EditUserUiEvent.OnSubmitClick -> {
                val user = UserData("", event.firstName, event.lastName, event.nickname, event.emails)
                store.updateUserData(user)
            }
        }
    }

}