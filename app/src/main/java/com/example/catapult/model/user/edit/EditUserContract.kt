package com.example.catapult.model.user.edit

import com.example.catapult.datastore.UserData

interface EditUserContract {

    data class EditUserState(
        val user: UserData = UserData()
    )

    sealed class EditUserUiEvent {
        data class OnSubmitClick(
            val avatar: String,
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val email: String
        ) : EditUserUiEvent()
    }

}