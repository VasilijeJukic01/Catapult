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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catapult.model.quiz.end_quiz.EndQuizViewModel
import com.example.catapult.model.quiz.end_quiz.EndQuizContract.*

// Navigation
fun NavGraphBuilder.quizEndScreen(
    route: String,
    navController: NavController,
) = composable(route = "$route/{totalPoints}/{category}") { backStackEntry ->
    val endQuizViewModel = hiltViewModel<EndQuizViewModel>()

    val totalPoints = backStackEntry.arguments?.getString("totalPoints")?.toFloat() ?: 0.0f
    val category = backStackEntry.arguments?.getString("category")?.toInt() ?: 0

    QuizEndScreen(
        totalScore = totalPoints,
        category = category,
        onHomeClick = {
            navController.navigate("choose") { popUpTo("choose") { inclusive = true } }
        },
        onSubmitClick = {
            navController.navigate("choose") { popUpTo("choose") { inclusive = true } }
        },
        eventPublisher = { endQuizViewModel.setEvent(it) }
    )
}

@Composable
fun QuizEndScreen(
    totalScore: Float,
    category: Int = 0,
    onHomeClick: () -> Unit,
    onSubmitClick: () -> Unit,
    eventPublisher: (EndQuizUIEvent) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Quiz Ended",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Total Score
            Text(
                text = "Total Score: $totalScore",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Submit Button
            Button(
                onClick = {
                    eventPublisher(EndQuizUIEvent.Submit(1, category, totalScore))
                    onSubmitClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text("Submit Result")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Home Button
            Button(
                onClick = {
                    eventPublisher(EndQuizUIEvent.Submit(0, category, totalScore))
                    onHomeClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text("Home")
            }
        }
    }
}
