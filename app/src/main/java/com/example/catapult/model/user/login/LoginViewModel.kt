package com.example.catapult.model.user.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.datastore.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.catapult.model.user.login.LoginContract.LoginUiEvent
import com.example.catapult.model.user.login.LoginContract.LoginState

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val store: UserStore
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(LoginState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: LoginState.() -> LoginState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<LoginUiEvent>()

    fun setEvent(event: LoginUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
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

    private suspend fun handleEvent(event: LoginUiEvent) {
        when (event) {
            // Login
            is LoginUiEvent.OnLoginClick -> {
                setState { copy(isLoading = true) }
                val user = UserData("", event.firstName, event.lastName, event.nickname, event.emails)
                store.updateUserData(user)
                setState { copy(isLoading = false, isLoggedIn = true) }
            }
        }
    }
}

