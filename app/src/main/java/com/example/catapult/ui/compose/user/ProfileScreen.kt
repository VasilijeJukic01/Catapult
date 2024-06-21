package com.example.catapult.ui.compose.user

import android.annotation.SuppressLint
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.datastore.UserData
import com.example.catapult.model.user.profile.ProfileContract
import com.example.catapult.model.user.profile.ProfileViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

// Navigation
fun NavGraphBuilder.profileScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController
) = composable(
    route = route,
    arguments = arguments,
    enterTransition = { slideInHorizontally { it } },
    exitTransition = { scaleOut (targetScale = 0.75f) },
    popEnterTransition = { scaleIn(initialScale = 0.75f) },
    popExitTransition = { slideOutHorizontally { it } },
) { backStackEntry ->
    val profileViewModel = hiltViewModel<ProfileViewModel>(backStackEntry)
    val state by profileViewModel.state.collectAsState()

    ProfileScreen(
        state = state,
        onBackClick = { navController.popBackStack() }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileContract.ProfileState,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Back",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Box {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    UserInfo(state.currentUser)
                    Spacer(modifier = Modifier.height(16.dp))
                    QuizHistory(state)
                    Spacer(modifier = Modifier.height(16.dp))
                    BestResults(state)
                    Spacer(modifier = Modifier.height(16.dp))
                    BestGlobalPositions(state)
                }
            }
        }
        }
    }


@Composable
fun UserInfo(user: UserData) {
    Text(
        "Profile Info:",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text("First Name: ${user.firstName}", style = MaterialTheme.typography.bodyLarge)
        Text("Last Name: ${user.lastName}", style = MaterialTheme.typography.bodyLarge)
        Text("Email: ${user.email}", style = MaterialTheme.typography.bodyLarge)
        Text("Nickname: ${user.nickname}", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun QuizHistory(state: ProfileContract.ProfileState) {
    Text("Quiz History:", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
    state.quizHistory.forEach { (categoryId, results) ->
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text("Category: $categoryId", style = MaterialTheme.typography.bodyLarge)
            results.forEach { result ->
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        "Pos: ${result.position}: ${String.format("%.2f",result.result)} points",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        formatTimestamp(result.createdAt),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun BestResults(state: ProfileContract.ProfileState) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            "Best Results:",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        state.bestResults.toList().forEachIndexed { index, result ->
            Text("Category ${index + 1}: $result", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BestGlobalPositions(state: ProfileContract.ProfileState) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            "Best Global Positions:",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        state.bestGlobalPositions.toList().forEachIndexed { index, position ->
            Text("Category ${index + 1}: $position", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

