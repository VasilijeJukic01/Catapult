package com.example.catapult.ui.compose.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.material3.Surface

// TODO: Add submit button logic

// Navigation
fun NavGraphBuilder.quizEndScreen(
    route: String,
    navController: NavController,
) = composable(route = "$route/{totalPoints}") { backStackEntry ->
    val totalPoints = backStackEntry.arguments?.getString("totalPoints")?.toFloat() ?: 0.0f
    QuizEndScreen(
        totalScore = totalPoints,
        onHomeClick = { navController.navigate("choose") { popUpTo("choose") { inclusive = true } } }
    )
}

@Composable
fun QuizEndScreen(
    totalScore: Float,
    onHomeClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quiz Ended",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Total Score: $totalScore",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onHomeClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text("Home")
            }
        }
    }
}
