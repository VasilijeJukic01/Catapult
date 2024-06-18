package com.example.catapult.ui.compose.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.catapult.model.user_drawer.UserDrawerDestination
import com.example.catapult.model.user_drawer.UserDrawerViewModel
import com.example.catapult.model.user_drawer.UserDrawerContract.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.ui.compose.avatar.getAvatarResource
import androidx.compose.material3.*

@Composable
fun UserDrawer(
    drawerState: DrawerState,
    onDrawerDestinationClick: (UserDrawerDestination) -> Unit,
    navController: NavController,
) {
    val uiScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<UserDrawerViewModel>()
    val state = viewModel.state.collectAsState()

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch {
            drawerState.close()
        }
    }

    if (state.value.loginRedirect) {
        navController.navigate("login")
    } else
        AppDrawer(
            state = state.value,
            onDrawerDestinationClick = {
                uiScope.launch { drawerState.close() }
                when (it) {
                    // Navigate to Profile
                    is UserDrawerDestination.Profile -> {
                        val userJson = Json.encodeToJsonElement(it.user)
                        navController.navigate("profile/$userJson")
                    }
                    // Navigate to Edit Profile
                    is UserDrawerDestination.EditProfile -> {
                        val userJson = Json.encodeToJsonElement(it.user)
                        navController.navigate("editUser/$userJson")
                    }
                    // Navigate to Add Profile
                    is UserDrawerDestination.AddProfile -> {
                        navController.navigate("addUser")
                    }
                    // Navigate to Leaderboard
                    is UserDrawerDestination.Leaderboard -> {
                        navController.navigate("leaderboard")
                    }
                }
                onDrawerDestinationClick(it)
            },
            eventPublisher = { viewModel.setEvent(it) },
            navController = navController,
        )
}

@Composable
fun AppDrawer(
    state: DrawerUiState,
    eventPublisher: (DrawerUiEvent) -> Unit,
    navController: NavController,
    onDrawerDestinationClick: (UserDrawerDestination) -> Unit,
) {
    val logoutDialog = remember { mutableStateOf(false) }
    val switchProfileDialog = remember { mutableStateOf(false) }

    // Dialog Handlers
    ShowLogoutDialog(logoutDialog, state, navController, eventPublisher)
    ShowSwitchProfileDialog(switchProfileDialog, state, eventPublisher)

    // Content
    BoxWithConstraints {
        ModalDrawerSheet(
            modifier = Modifier.width(maxWidth * 3 / 4),
        ) {
            // Core
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Catapult",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDrawerDestinationClick(UserDrawerDestination.Profile(state.currentAccount.nickname)) }
                            .padding(16.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val avatarUri = state.currentAccount.avatar
                            val avatar: Painter = if (avatarUri.contains("@Default")) {
                                painterResource(id = getAvatarResource(avatarUri))
                            } else {
                                rememberAsyncImagePainter(model = avatarUri)
                            }
                            Image(
                                painter = avatar,
                                contentDescription = "Avatar Icon",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = state.currentAccount.nickname,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                    // Add Profile
                    AppDrawerActionItem(
                        icon = Icons.Default.Add,
                        text = "Add Profile",
                        onClick = { onDrawerDestinationClick(UserDrawerDestination.AddProfile) },
                    )
                    // Edit Profile
                    AppDrawerActionItem(
                        icon = Icons.Default.Edit,
                        text = "Edit Profile",
                        onClick = { onDrawerDestinationClick(UserDrawerDestination.EditProfile(state.currentAccount.nickname)) },
                    )
                    // Switch Profile
                    AppDrawerActionItem(
                        icon = Icons.Default.Face,
                        text = "Switch Profile",
                        onClick = { switchProfileDialog.value = true },
                    )
                    // Leaderboard
                    AppDrawerActionItem(
                        icon = Icons.Default.Star,
                        text = "Leaderboard",
                        onClick = { onDrawerDestinationClick(UserDrawerDestination.Leaderboard) },
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth())
                // Logout
                AppDrawerActionItem(
                    icon = Icons.Default.ExitToApp,
                    text = "Logout",
                    onClick = { logoutDialog.value = true },
                )
            }
        }
    }
}

// Drawer Action Item
@Composable
fun AppDrawerActionItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}