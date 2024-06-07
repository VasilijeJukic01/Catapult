package com.example.catapult.ui.compose.quiz

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.quiz.guess_cat.GuessCatContract
import com.example.catapult.model.quiz.guess_cat.GuessCatViewModel
import com.example.catapult.model.quiz.guess_cat.GuessCatContract.GuessTheCatUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessTheCatScreen(
    state: GuessCatContract.GuessTheCatState,
    onCatImageClick: (Int) -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit
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
                modifier = Modifier.fillMaxWidth().padding(16.dp),
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
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    state.catImages.take(2).forEachIndexed { index, catImage ->
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(catImage.url),
                                contentDescription = "Cat Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onCatImageClick(index) }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    state.catImages.drop(2).forEachIndexed { index, catImage ->
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(catImage.url),
                                contentDescription = "Cat Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onCatImageClick(index + 2) }
                            )
                        }
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
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
            )
        }
    }
}


fun NavGraphBuilder.guessTheCatScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val guessCatViewModel = viewModel<GuessCatViewModel>()

    val state by guessCatViewModel.state.collectAsState()

    if (state.timeLeft == 0L) {
        // TODO: Navigate to Time Up Screen
    }
    else {
        GuessTheCatScreen(
            state = state,
            onCatImageClick = { index ->
                guessCatViewModel.setEvent(GuessTheCatUiEvent.SelectCatImage(index))
            },
            onSkipClick = {
                guessCatViewModel.setEvent(GuessTheCatUiEvent.NextQuestion(false))
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGuessTheCatScreen() {
    GuessTheCatScreen(
        state = GuessCatContract.GuessTheCatState(
            question = "Which cat is playful?",
            catImages = listOf(
                UIBreedImage(url="https://cdn2.thecatapi.com/images/5p0.jpg"),
                UIBreedImage(url="https://cdn2.thecatapi.com/images/5p0.jpg"),
                UIBreedImage(url="https://cdn2.thecatapi.com/images/5p0.jpg"),
                UIBreedImage(url="https://cdn2.thecatapi.com/images/5p0.jpg"),
            )
        ),
        onCatImageClick = {},
        onSkipClick = {},
        onBackClick = {}
    )
}

