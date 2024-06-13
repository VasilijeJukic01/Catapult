package com.example.catapult.model.user.edit

import com.example.catapult.datastore.UserData

interface EditUserContract {

    data class EditUserState(
        val currentUser: UserData,
        val isError: Boolean = false,
        val errorMessage: String = ""
    )

    sealed class EditUserUiEvent {
        data class OnSubmitClick(val firstName: String, val lastName: String, val nickname: String, val emails: String) : EditUserUiEvent()
    }
}