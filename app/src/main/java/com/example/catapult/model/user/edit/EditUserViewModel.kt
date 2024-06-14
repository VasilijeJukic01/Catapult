package com.example.catapult.model.user.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.datastore.UserStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.catapult.model.user.edit.EditUserContract.*
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val store: UserStore,
) : ViewModel() {

    // Event
    private val eventsFlow = MutableSharedFlow<EditUserUiEvent>()

    fun setEvent(event: EditUserUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
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
            // Change Profile
            is EditUserUiEvent.OnSubmitClick -> {
                val user = UserData("", event.firstName, event.lastName, event.nickname, event.emails)
                store.updateUserData(user)
            }
        }
    }

}