package com.example.catapult.ui.compose.user

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.R
import com.example.catapult.model.user.login.LoginViewModel
import com.example.catapult.model.user.login.LoginContract.LoginUiEvent
import com.example.catapult.model.user.login.LoginContract.LoginUiEvent.OnLoginClick
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.example.catapult.ui.compose.transparentTextField

// Navigation
fun NavGraphBuilder.loginScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val state by loginViewModel.state.collectAsState()

    if (state.isLoggedIn) {
        navController.navigate("choose")
    } else {
        LoginScreen(eventPublisher = { loginViewModel.setEvent(it) })
    }
}

@Composable
fun LoginScreen(
    eventPublisher: (LoginUiEvent) -> Unit
) {
    val image = painterResource(id = R.drawable.background)

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Regex
    val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$".toRegex()
    val nicknamePattern = "^[a-zA-Z0-9_]+$".toRegex()

    val isNicknameError = !nicknamePattern.matches(nickname) && nickname.isNotEmpty()
    val isEmailError = !emailPattern.matches(email) && email.isNotEmpty()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        // Form Surface
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // First Name
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(
                        text = "First Name",
                        color = Color.Black
                    ) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = transparentTextField()
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Last Name
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text(
                        text = "Last Name",
                        color = Color.Black
                    ) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = transparentTextField()
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Nickname
                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text(
                        text = "Nickname",
                        color = Color.Black
                    ) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isNicknameError,
                    colors = transparentTextField(isError = isNicknameError)
                )
                if (isNicknameError) {
                    Text(
                        text = "Invalid nickname",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Email
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(
                        text = "Email",
                        color = Color.Black
                    ) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isEmailError,
                    colors = transparentTextField(isError = isEmailError)
                )
                if (isEmailError) {
                    Text(
                        text = "Invalid email address",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        if (!isEmailError && !isNicknameError) {
                            eventPublisher(OnLoginClick(firstName, lastName, nickname, email))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                    )
                }
            }
        }
    }
}