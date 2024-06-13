import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.datastore.UserData
import com.example.catapult.model.user.profile.ProfileContract
import com.example.catapult.model.user.profile.ProfileViewModel
import kotlinx.serialization.json.Json

// Navigation
fun NavGraphBuilder.profileScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController
) = composable(
    route = route,
    arguments = arguments
) { backStackEntry ->
    val userJson = backStackEntry.arguments?.getString("user")
    val user = Json.decodeFromString<UserData>(userJson ?: "")

    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val state by profileViewModel.state.collectAsState()

    ProfileScreen(user = user, navController = navController, state = state)
}

@Composable
fun ProfileScreen(
    user: UserData,
    navController: NavController,
    state: ProfileContract.ProfileState
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "First Name: ${user.firstName}")
        Text(text = "Last Name: ${user.lastName}")
        Text(text = "Email: ${user.email}")
        Text(text = "Nickname: ${user.nickname}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Quiz History:", style = MaterialTheme.typography.bodyMedium)
        state.quizResults.forEach { result ->
            Text(text = "${result.position}: ${result.result} points")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Best Results: ${state.bestResults}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Best Global Positions: ${state.bestGlobalPositions}")
    }
}
