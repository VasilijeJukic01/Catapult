package com.example.catapult.model.user_drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserData
import com.example.catapult.datastore.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.catapult.model.user_drawer.UserDrawerContract.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class UserDrawerViewModel @Inject constructor(
    private val userStore: UserStore,
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(DrawerUiState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: DrawerUiState.() -> DrawerUiState) = stateFlow.update(reducer)

    // Events
    private val eventsFlow = MutableSharedFlow<DrawerUiEvent>()

    fun setEvent(event: DrawerUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
        fetchUsersDrawerData(userStore)
    }

    private fun handleEvents() {
        viewModelScope.launch {
            eventsFlow
                .collect { event ->
                    handleEvent(event)
                }
        }
    }

    private fun handleEvent(event: DrawerUiEvent) {
        when (event) {
            // Switch Account
            is DrawerUiEvent.SwitchAccount -> {
                viewModelScope.launch {
                    switchUser(event.user)
                }
            }
            // Logout
            is DrawerUiEvent.Logout -> {
                viewModelScope.launch {
                    userStore.deleteUser(event.user)
                    val remainingUsers = userStore.getAllUsers().first()
                    if (remainingUsers.isNotEmpty()) {
                        userStore.switchUser(remainingUsers.first())
                    }
                    else
                        setState { copy(currentAccount = UserData(), accounts = emptyFlow(), loginRedirect = true)}
                }
            }
        }
    }

    // Fetch
    private fun fetchUsersDrawerData(userStore: UserStore) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val usersFlow = userStore.getAllUsers()
                usersFlow.collect { users ->
                    val currentUser = users.find { it.active == 1 }
                    setState {
                        copy(
                            currentAccount = currentUser ?: UserData(),
                            accounts = usersFlow
                        )
                    }
                }
            }
        }
    }

    private fun switchUser(user: UserData) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userStore.switchUser(user)
                setState { copy(currentAccount = user)}
            }
        }
    }

}

