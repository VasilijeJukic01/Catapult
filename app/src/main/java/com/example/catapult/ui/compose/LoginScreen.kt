package com.example.catapult.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.model.user.LoginViewModel
import com.example.catapult.model.user.LoginContract.LoginUiEvent.OnLoginClick

// TODO: Colors and design

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$".toRegex()
    val nicknamePattern = "^[a-zA-Z0-9_]+$".toRegex()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Nickname") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = !nicknamePattern.matches(nickname) && nickname.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = !emailPattern.matches(email) && email.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (emailPattern.matches(email) && nicknamePattern.matches(nickname)) {
                    viewModel.setEvent(OnLoginClick(firstName, lastName, nickname, email))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}

// Navigation
fun NavGraphBuilder.loginScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val loginViewModel = hiltViewModel<LoginViewModel>()

    val state by loginViewModel.state.collectAsState()

    if (state.isLoggedIn) {
        navController.navigate("choose")
    }
    else {
        LoginScreen(
            viewModel = loginViewModel
        )
    }

}