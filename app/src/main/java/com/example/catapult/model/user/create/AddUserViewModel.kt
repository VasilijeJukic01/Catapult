package com.example.catapult.model.user.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.datastore.UserStore
import com.example.catapult.model.user.create.AddUserContract.*
import com.example.catapult.ui.compose.avatar.getRandomAvatar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val store: UserStore,
) : ViewModel() {

    // Event
    private val eventsFlow = MutableSharedFlow<AddUserUiEvent>()

    fun setEvent(event: AddUserUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

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
            // Add Profile
            is AddUserUiEvent.AddUser -> {
                val user = UserData(getRandomAvatar(), event.firstName, event.lastName, event.nickname, event.email, 1)
                store.addUserData(user)
            }
        }
    }
}
