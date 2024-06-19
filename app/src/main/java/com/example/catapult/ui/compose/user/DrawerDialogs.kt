package com.example.catapult.ui.compose.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.model.user_drawer.UserDrawerContract.*
import com.example.catapult.ui.compose.avatar.getAvatarResource

@Composable
fun ShowSwitchProfileDialog(
    switchProfileDialog: MutableState<Boolean>,
    state: DrawerUiState,
    eventPublisher: (DrawerUiEvent) -> Unit
) {
    if (switchProfileDialog.value) {
        AlertDialog(
            onDismissRequest = { switchProfileDialog.value = false },
            title = { Text("Switch Profile") },
            text = {
                val users = state.accounts.collectAsState(initial = emptyList()).value
                LazyColumn {
                    items(users.size) { index ->
                        val user = users[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    eventPublisher(DrawerUiEvent.SwitchAccount(user))
                                    switchProfileDialog.value = false
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val avatarUri = user.avatar
                            val avatar: Painter = if (avatarUri.contains("@Default")) {
                                painterResource(id = getAvatarResource(avatarUri))
                            } else {
                                rememberAsyncImagePainter(model = avatarUri)
                            }
                            Image(
                                painter = avatar,
                                contentDescription = "Avatar Icon",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = user.nickname,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun ShowLogoutDialog(
    logoutDialog: MutableState<Boolean>,
    state: DrawerUiState,
    navController: NavController,
    eventPublisher: (DrawerUiEvent) -> Unit
) {
    val remainingUsers = state.accounts.collectAsState(initial = emptyList()).value

    if (logoutDialog.value) {
        AlertDialog(
            onDismissRequest = { logoutDialog.value = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = {
                    logoutDialog.value = false
                    eventPublisher(DrawerUiEvent.Logout(state.currentAccount))
                    if (state.loginRedirect) {
                        navController.navigate("login")
                    } else {
                        eventPublisher(DrawerUiEvent.SwitchAccount(remainingUsers.first()))
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { logoutDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}