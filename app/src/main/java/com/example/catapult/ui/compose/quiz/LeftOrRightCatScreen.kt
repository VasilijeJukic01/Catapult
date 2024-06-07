package com.example.catapult.ui.compose.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract
import com.example.catapult.model.quiz.left_or_right.LeftOrRightViewModel
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract.LeftOrRightUiEvent

@Composable
fun LeftOrRightScreen(
    state: LeftOrRightContract.LeftOrRightState,
    onCatImageClick: (Int) -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    LeftOrRightContent(
        state = state,
        onCatImageClick = onCatImageClick,
        onSkipClick = onSkipClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeftOrRightContent(
    state: LeftOrRightContract.LeftOrRightState,
    onCatImageClick: (Int) -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val correctColor = Color.Green
    val incorrectColor = Color.Red

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text("Quiz") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Question ${state.currentQuestionNumber} of 20",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total Points: ${state.totalCorrect}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Time Left: ${state.timeLeft / 60}:${state.timeLeft % 60}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = state.question,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                state.catImages.toList().forEachIndexed { index, catImage ->
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .clickable { onCatImageClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(catImage.url),
                            contentDescription = "Cat Image",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Button(onClick = onSkipClick, modifier = Modifier.padding(16.dp)) {
                Text("Skip Question")
            }
        }

        state.isCorrectAnswer?.let { isCorrect ->
            val color = if (isCorrect) correctColor else incorrectColor
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color.copy(alpha = 0.3f))
            )
        }
    }
}

fun NavGraphBuilder.leftOrRightScreen(
    route: String,
    navController: NavController,
) {
    composable(route = route) {
        val leftOrRightViewModel: LeftOrRightViewModel = viewModel()
        val state by leftOrRightViewModel.state.collectAsState()
        val navigateToEndQuiz by leftOrRightViewModel.navigateToEndQuiz.collectAsState()

        LeftOrRightScreen(
            state = state,
            onCatImageClick = { index ->
                leftOrRightViewModel.setEvent(LeftOrRightUiEvent.SelectLeftOrRight(index))
            },
            onSkipClick = {
                leftOrRightViewModel.setEvent(LeftOrRightUiEvent.NextQuestion(false))
            }
        ) {
            navController.popBackStack()
        }

        LaunchedEffect(navigateToEndQuiz) {
            navigateToEndQuiz?.let {
                navController.navigate("quizEndScreen/$it")
            }
        }
    }
}

