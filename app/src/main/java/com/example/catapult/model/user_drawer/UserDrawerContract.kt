package com.example.catapult.model.user_drawer

import com.example.catapult.datastore.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface UserDrawerContract {

    data class DrawerUiState(
        val currentAccount: UserData = UserData(),
        val accounts: Flow<List<UserData>> = flowOf(emptyList()),
        val loginRedirect : Boolean = false
    )
    sealed class DrawerUiEvent {
        data class SwitchAccount(val user: UserData) : DrawerUiEvent()
        data class Logout(val user: UserData) : DrawerUiEvent()
    }

}