package com.example.catapult.navigation

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.datastore.UserStore
import com.example.catapult.ui.compose.ChooseScreen
import com.example.catapult.ui.compose.catalog.breedDetailsScreen
import com.example.catapult.ui.compose.catalog.breedsListScreen
import com.example.catapult.ui.compose.catalog.breedGalleryScreen
import com.example.catapult.ui.compose.catalog.breedImagesGrid
import com.example.catapult.ui.compose.loginScreen
import com.example.catapult.ui.compose.quiz.guessTheCatScreen
import com.example.catapult.ui.compose.quiz.guessTheFactScreen
import com.example.catapult.ui.compose.quiz.leftOrRightScreen
import com.example.catapult.ui.compose.quiz.quizEndScreen

@Composable
fun AppNavigation(userStore: UserStore) {
    val navController = rememberNavController()
    val startDestination = remember { mutableStateOf("loading") }

    // TODO: Fix architecture

    LaunchedEffect(userStore) {
        if (userStore.isUserLoggedIn()) {
            startDestination.value = "choose"
        } else {
            startDestination.value = "login"
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination.value,
        enterTransition = { slideInHorizontally {it } },
        exitTransition = { scaleOut (targetScale = 0.75f) },
        popEnterTransition = { scaleIn(initialScale = 0.75f) },
        popExitTransition = { slideOutHorizontally { it} },
    ) {
        composable("loading") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        composable("choose") {
            ChooseScreen(navController = navController)
        }
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
    }
}
