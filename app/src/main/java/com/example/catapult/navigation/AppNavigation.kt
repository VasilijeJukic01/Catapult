package com.example.catapult.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.ui.compose.ChooseScreen
import com.example.catapult.ui.compose.breedDetailsScreen
import com.example.catapult.ui.compose.breedsListScreen
import com.example.catapult.ui.compose.breedGalleryScreen
import com.example.catapult.ui.compose.breedImagesGrid
import com.example.catapult.ui.compose.quiz.guessTheCatScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "choose",
    ) {
        // Routes
        composable("choose") {
            ChooseScreen(navController = navController)
        }
        breedsListScreen (
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
        guessTheCatScreen(
            route = "guessTheCat",
            navController = navController,
        )
    }

}