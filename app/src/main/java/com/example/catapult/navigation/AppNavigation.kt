package com.example.catapult.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.datastore.UserStore
import com.example.catapult.ui.compose.leaderboard.leaderboardScreen
import com.example.catapult.ui.compose.user.loginScreen
import com.example.catapult.ui.compose.catalog.*
import com.example.catapult.ui.compose.chooseScreen
import com.example.catapult.ui.compose.quiz.*
import com.example.catapult.ui.compose.user.addUserScreen
import com.example.catapult.ui.compose.user.editUserScreen
import com.example.catapult.ui.compose.user.profileScreen

@Composable
fun AppNavigation(userStore: UserStore) {
    val navController = rememberNavController()
    val startDestination = remember { mutableStateOf("loading") }

    LaunchedEffect(userStore) {
        startDestination.value = if (userStore.isUserLoggedIn()) "choose" else "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination.value,
     //   enterTransition = { slideInHorizontally {it } },
      //  exitTransition = { scaleOut (targetScale = 0.75f) },
       // popEnterTransition = { scaleIn(initialScale = 0.75f) },
      //  popExitTransition = { slideOutHorizontally { it} },
    ) {
        composable(route = "loading") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        chooseScreen(
            route = "choose",
            navController = navController
        )
        loginScreen(
            route = "login",
            navController = navController,
        )
        breedsListScreen(
            route = "breeds",
            navController = navController,
        )
        breedDetailsScreen(
            route = "breeds/{id}",
            navController = navController,
        )
        breedImagesGrid(
            route = "breeds/grid/{breedId}",
            navController = navController,
            arguments = listOf(
                navArgument(name = "breedId") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = { navController.navigateUp() }
        )
        breedGalleryScreen(
            route = "breeds/gallery/{breedId}?currentImage={currentImage}",
            arguments = listOf(
                navArgument(name = "breedId") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument(name = "currentImage") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = { navController.navigateUp() }
        )
        guessTheFactScreen(
            route = "guessTheFact",
            navController = navController,
        )
        guessTheCatScreen(
            route = "guessTheCat",
            navController = navController,
        )
        leftOrRightScreen(
            route = "leftOrRight",
            navController = navController,
        )
        quizEndScreen(
            route = "quizEndScreen",
            navController = navController,
        )
        leaderboardScreen(
            route = "leaderboard",
            navController = navController,
        )
        profileScreen(
            route = "profile/{user}",
            arguments = listOf(
                navArgument("user") { type = NavType.StringType }
            ),
            navController = navController
        )
        editUserScreen(
            route = "editUser/{user}",
            arguments = listOf(
                navArgument("user") { type = NavType.StringType }
            ),
            navController = navController
        )
        addUserScreen(
            route = "addUser",
            navController = navController,
        )
    }
}
