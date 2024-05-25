package com.example.catapult.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.catapult.compose.screens.catalog.breedDetailsScreen
import com.example.catapult.compose.screens.catalog.breedsListScreen

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
    }

}