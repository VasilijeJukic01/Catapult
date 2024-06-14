package com.example.catapult.model.user.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.datastore.UserStore
import com.example.catapult.model.user.create.AddUserContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val store: UserStore,
) : ViewModel() {

    // Event
    private val eventsFlow = MutableSharedFlow<AddUserUiEvent.OnSubmitClick>()

    fun setEvent(event: AddUserUiEvent.OnSubmitClick) =
        viewModelScope.launch { eventsFlow.emit(event) }

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

    private suspend fun handleEvent(event: AddUserUiEvent) {
        when (event) {
            // Change Profile
            is AddUserUiEvent.OnSubmitClick -> {
                val user = UserData(event.avatar, event.firstName, event.lastName, event.nickname, event.email, 1)
                store.addUserData(user)
            }
        }
    }
}
