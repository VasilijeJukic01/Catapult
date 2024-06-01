package com.example.catapult.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.compose.screens.catalog.breedDetailsScreen
import com.example.catapult.compose.screens.catalog.breedsListScreen
import com.example.catapult.model.catalog.gallery.breedGalleryScreen
import com.example.catapult.model.catalog.grid.breedImagesGrid

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "breeds",
    ) {
        // Routes
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
            arguments = listOf(
                navArgument(name = "breedId") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            navController = navController,
            onClose = {
                navController.navigateUp()
            }
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
            onClose = {
                navController.navigateUp()
            }
        )
    }

}