package com.example.catapult.model.user_drawer

import com.example.catapult.datastore.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface UserDrawerContract {

    data class DrawerUiState(
        val currentAccount: UserData = UserData(),
        val accounts: Flow<List<UserData>> = flowOf(emptyList())
    )
    sealed class DrawerUiEvent {
        data object SwitchAccount : DrawerUiEvent()
        data class AddAccount(val user: UserData) : DrawerUiEvent()
        data class Logout(val user: UserData) : DrawerUiEvent()
    }

}