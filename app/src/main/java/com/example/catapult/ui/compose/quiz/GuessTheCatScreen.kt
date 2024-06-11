package com.example.catapult.ui.compose.quiz

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@SuppressLint("UnusedContentLambdaTargetStateParameter")
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

    val orientation = LocalConfiguration.current.orientation

    Surface(modifier = Modifier.fillMaxSize()) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
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

                AnimatedContent(
                    targetState = state.currentQuestionNumber,
                    transitionSpec = {
                        slideInHorizontally { it }.togetherWith(slideOutHorizontally { -it })
                    },
                    label = "QuestionAnimation"
                ) {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .padding(all = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.question,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                AnimatedContent(
                    targetState = state.currentQuestionNumber,
                    transitionSpec = {
                        slideInHorizontally { it }.togetherWith(slideOutHorizontally { -it })
                    },
                    label = "QuestionAnimation"
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.catImages.size) { index ->
                            val catImage = state.catImages[index]
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clickable { onCatImageClick(index) }
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(catImage.url),
                                    contentDescription = "Cat Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = onSkipClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Skip Question")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TopAppBar(
                    modifier = Modifier.height(80.dp),
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Menu")
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
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
                AnimatedContent(
                    targetState = state.currentQuestionNumber,
                    transitionSpec = {
                        slideInHorizontally { it }.togetherWith(slideOutHorizontally { -it })
                    },
                    label = "QuestionAnimation"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(600.dp)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.question,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = onSkipClick,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Skip Question")
                            }
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(2.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(2f)
                        ) {
                            items(state.catImages.size) { index ->
                                val catImage = state.catImages[index]
                                Box(
                                    modifier = Modifier
                                        .size(112.dp)
                                        .clickable { onCatImageClick(index) }
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(catImage.url),
                                        contentDescription = "Cat Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
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

// Navigation
fun NavGraphBuilder.guessTheCatScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val guessCatViewModel = viewModel<GuessCatViewModel>()

    val state by guessCatViewModel.state.collectAsState()

    if (state.quizEnded) {
        navController.navigate("quizEndScreen/${state.totalPoints}")
    } else {
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
                UIBreedImage(url = "https://cdn2.thecatapi.com/images/5p0.jpg"),
                UIBreedImage(url = "https://cdn2.thecatapi.com/images/5p0.jpg"),
                UIBreedImage(url = "https://cdn2.thecatapi.com/images/5p0.jpg"),
                UIBreedImage(url = "https://cdn2.thecatapi.com/images/5p0.jpg"),
            )
        ),
        onCatImageClick = {},
        onSkipClick = {},
        onBackClick = {}
    )
}

