package com.example.catapult.ui.compose.user

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.catapult.model.user.edit.EditUserContract.EditUserUiEvent.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NamedNavArgument
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.R
import com.example.catapult.model.user.edit.EditUserContract
import com.example.catapult.model.user.edit.EditUserContract.EditUserUiEvent
import com.example.catapult.model.user.edit.EditUserViewModel
import com.example.catapult.ui.compose.SetScreenOrientation
import com.example.catapult.ui.compose.avatar.copyImageToAppDir
import com.example.catapult.ui.compose.avatar.getAvatarResource
import com.example.catapult.ui.compose.transparentTextField
import com.example.catapult.ui.theme.topBarColor

// Navigation
fun NavGraphBuilder.editUserScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(
    route = route,
    arguments = arguments,
    enterTransition = { slideInHorizontally { it } },
    exitTransition = { scaleOut (targetScale = 0.75f) },
    popEnterTransition = { scaleIn(initialScale = 0.75f) },
    popExitTransition = { slideOutHorizontally { it } },
) { backStackEntry ->
    val editUserViewModel = hiltViewModel<EditUserViewModel>(backStackEntry)
    val state by editUserViewModel.state.collectAsState()

    EditUserScreen(
        eventPublisher = { editUserViewModel.setEvent(it) },
        state = state,
        onBackClick = { navController.popBackStack() },
        onSubmitClick = { navController.navigate("choose") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
    eventPublisher: (EditUserUiEvent) -> Unit,
    state: EditUserContract.EditUserState,
    onBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {},
) {
    SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val image = painterResource(id = R.drawable.background2)

    var firstName by remember { mutableStateOf(state.user.firstName) }
    var lastName by remember { mutableStateOf(state.user.lastName) }
    var nickname by remember { mutableStateOf(state.user.nickname) }
    var email by remember { mutableStateOf(state.user.email) }

    LaunchedEffect(state.user) {
        firstName = state.user.firstName
        lastName = state.user.lastName
        nickname = state.user.nickname
        email = state.user.email
    }

    // Avatar
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    // READ_EXTERNAL_STORAGE Runtime Permission Needed
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            val copiedImageUri = uri?.let {
                copyImageToAppDir(
                    context, context.contentResolver,
                    it, "selectedImage"
                )
            }
            if (copiedImageUri != null) {
                selectedImageUri = copiedImageUri
            }
        }

    // Regex
    val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$".toRegex()
    val nicknamePattern = "^[a-zA-Z0-9_]+$".toRegex()

    val isNicknameError = !nicknamePattern.matches(nickname) && nickname.isNotEmpty()
    val isEmailError = !emailPattern.matches(email) && email.isNotEmpty()

    Box(
        modifier = Modifier.fillMaxSize(),
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
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // Avatar
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val avatar: Painter = selectedImageUri?.let { uri ->
                            rememberAsyncImagePainter(model = uri)
                        } ?: if (state.user.avatar.contains("@Default")) {
                            painterResource(id = getAvatarResource(state.user.avatar))
                        } else {
                            rememberAsyncImagePainter(model = state.user.avatar)
                        }
                        Image(
                            painter = avatar,
                            contentDescription = "Avatar Icon",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { launcher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
                        ) {
                            Text(
                                text = "Choose Photo",
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    // First Name
                    TextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = {
                            Text(
                                text = "First Name",
                                color = Color.Black
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = transparentTextField()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Last Name
                    TextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = {
                            Text(
                                text = "Last Name",
                                color = Color.Black
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = transparentTextField()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Nickname
                    TextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = {
                            Text(
                                text = "Nickname",
                                color = Color.Black
                            )
                        },
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
                        label = {
                            Text(
                                text = "Email",
                                color = Color.Black
                            )
                        },
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
                                val avatar = if (selectedImageUri != null) selectedImageUri.toString() else state.user.avatar
                                eventPublisher(
                                    EditUser(
                                        avatar,
                                        firstName,
                                        lastName,
                                        nickname,
                                        email
                                    )
                                )
                                onSubmitClick()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Submit",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}