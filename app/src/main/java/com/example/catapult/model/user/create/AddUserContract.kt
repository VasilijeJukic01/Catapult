package com.example.catapult.model.user.create

interface AddUserContract {

    sealed class AddUserUiEvent {
        data class AddUser(
            val avatar: String,
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val email: String,
        ) : AddUserUiEvent()
    }

}