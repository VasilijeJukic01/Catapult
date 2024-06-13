package com.example.catapult.model.user_drawer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.datastore.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.catapult.model.user_drawer.UserDrawerContract.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class UserDrawerViewModel @Inject constructor(
    private val userStore: UserStore,
) : ViewModel() {

    private val _state = MutableStateFlow(DrawerUiState())
    val state = _state.asStateFlow()

    private fun setState(reducer: DrawerUiState.() -> DrawerUiState) = _state.update(reducer)

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
            is DrawerUiEvent.SwitchAccount -> {
                // TODO
            }

            is DrawerUiEvent.AddAccount -> {
                // TODO
            }

            is DrawerUiEvent.Logout -> {
                Log.d("UserDrawerViewModel before", "Logout")
                viewModelScope.launch {
                    userStore.deleteUser()
                }
                Log.d("UserDrawerViewModel after", "Logout")

            }
        }
    }

    private fun fetchUsersDrawerData(userStore: UserStore) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val usersFlow = userStore.getAllUsers()
                usersFlow.collect { users ->
                    Log.d("UserDrawerViewModel", "Users: $users")
                    setState {
                        copy(
                            currentAccount = users.first(),
                            accounts = usersFlow
                        )
                    }
                }
            }
        }
    }

}

