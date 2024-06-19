package com.example.catapult.ui.compose.user

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.model.user.create.AddUserContract.AddUserUiEvent.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.catapult.R
import com.example.catapult.model.user.create.AddUserContract.AddUserUiEvent
import com.example.catapult.model.user.create.AddUserViewModel
import com.example.catapult.ui.compose.transparentTextField
import com.example.catapult.ui.theme.topBarColor

// Navigation
fun NavGraphBuilder.addUserScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val addUserViewModel = hiltViewModel<AddUserViewModel>()

    AddProfileScreen(
        eventPublisher = { addUserViewModel.setEvent(it) },
        onBackClick = { navController.popBackStack() },
        onSubmitClick = { navController.navigate("choose") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProfileScreen(
    eventPublisher: (AddUserUiEvent) -> Unit,
    onBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {},
) {
    val image = painterResource(id = R.drawable.background2)

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
        modifier = Modifier.fillMaxSize()
    ) {
        // Background
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Column {
            // TopAppBar
            TopAppBar(
                title = {
                    Text(
                        text = "Back",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",tint = Color.Black)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Surface
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color.Transparent
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Add Profile",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // First Name
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
                                eventPublisher(AddUser("", firstName, lastName, nickname, email))
                                onSubmitClick()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Add Profile",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}
