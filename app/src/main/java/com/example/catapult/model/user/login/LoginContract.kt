package com.example.catapult.model.user.login

interface LoginContract {

    data class LoginState(
        val isLoggedIn: Boolean = false,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = ""
    )

    sealed class LoginUiEvent {
        data class OnLoginClick(val firstName: String, val lastName: String, val nickname: String, val emails: String) : LoginUiEvent()
    }
}