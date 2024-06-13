package com.example.catapult.ui.compose.user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun UserDrawer(
    drawerState: DrawerState,
    onDrawerDestinationClick: (UserDrawerDestination) -> Unit,
    navController: NavController,
) {
    val uiScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<UserDrawerViewModel>()

    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch {
            drawerState.close()
        }
    }
    val uiState = viewModel.state.collectAsState()

    AppDrawer(
        state = uiState.value,
        onDrawerDestinationClick = {
            uiScope.launch { drawerState.close() }
            val destination = when (it) {
                is UserDrawerDestination.Profile -> {
                    val userJson = Json.encodeToJsonElement(it.user)
                    navController.navigate("profile/$userJson")
                }
                is UserDrawerDestination.EditProfile -> {
                    val userJson = Json.encodeToJsonElement(it.user)
                    navController.navigate("editUser/$userJson")
                }
            }
            onDrawerDestinationClick(it)
        },
        eventPublisher = {
            // TODO Publish event
        },
        navController = navController,
    )
}

@Composable
fun AppDrawer(
    state: DrawerUiState,
    eventPublisher: (DrawerUiEvent) -> Unit,
    navController: NavController,
    onDrawerDestinationClick: (UserDrawerDestination) -> Unit,
    userDrawerViewModel: UserDrawerViewModel = hiltViewModel(),
) {

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    userDrawerViewModel.setEvent(DrawerUiEvent.Logout(state.currentAccount))
                    navController.navigate("login")
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    BoxWithConstraints {
        ModalDrawerSheet(
            modifier = Modifier.width(maxWidth * 3 / 4),
        ) {
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
                    AppDrawerActionItem(
                        icon = Icons.Default.Person,
                        text = state.currentAccount.nickname,
                        onClick = { onDrawerDestinationClick(UserDrawerDestination.Profile(state.currentAccount)) },
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Add,
                        text = "Add Profile",
                        onClick = {  userDrawerViewModel.setEvent(DrawerUiEvent.AddAccount(state.currentAccount))},
                    )

                    AppDrawerActionItem(
                        icon = Icons.Default.Edit,
                        text = "Edit Profile",
                        onClick = { onDrawerDestinationClick(UserDrawerDestination.EditProfile(state.currentAccount)) },
                    )
                }

                Divider(modifier = Modifier.fillMaxWidth())

                AppDrawerActionItem(
                    icon = Icons.Default.ExitToApp,
                    text = "Logout",
                    onClick = { showDialog.value = true },
                )
            }
        }
    }
}

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