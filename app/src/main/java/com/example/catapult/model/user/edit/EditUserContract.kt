package com.example.catapult.model.user.edit

interface EditUserContract {

    sealed class EditUserUiEvent {
        data class OnSubmitClick(
            val avatar: String,
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val emails: String
        ) : EditUserUiEvent()
    }

}