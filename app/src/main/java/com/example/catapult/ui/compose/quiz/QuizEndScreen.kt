package com.example.catapult.ui.compose.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Composable
fun QuizEndScreen(
    totalScore: Float,
    onHomeClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
        Button(onClick = onHomeClick) {
            Text("Home")
        }
    }
}
fun NavGraphBuilder.quizEndScreen(
    route: String,
    navController: NavController,
) {
    composable(route = "$route/{totalPoints}") { backStackEntry ->
        val totalPoints = backStackEntry.arguments?.getString("totalPoints")?.toFloat() ?: 0.0f
        QuizEndScreen(
            totalScore = totalPoints,
            onHomeClick = { navController.navigate("choose") { popUpTo("choose") { inclusive = true } } }
        )
    }
}