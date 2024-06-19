package com.example.catapult.ui.compose.quiz

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract.LeftOrRightUiEvent
import com.example.catapult.ui.compose.ShowExitQuizDialog
import com.example.catapult.model.quiz.left_or_right.*

// Navigation
fun NavGraphBuilder.leftOrRightScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val leftOrRightViewModel: LeftOrRightViewModel = hiltViewModel()
    val state by leftOrRightViewModel.state.collectAsState()

    if (state.quizEnded) {
        navController.navigate("quizEndScreen/${state.totalPoints}/${3}")
    } else {
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
    }
}

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

@SuppressLint("UnusedContentLambdaTargetStateParameter")
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
    val showDialog = remember { mutableStateOf(false) }

    val orientation = LocalConfiguration.current.orientation

    ShowExitQuizDialog(showDialog, onBackClick)
    BackHandler(onBack = { showDialog.value = true })

    Surface(modifier = Modifier.fillMaxSize()) {
        // Portrait
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = { Text("Quiz") },
                    navigationIcon = {
                        IconButton(onClick = { showDialog.value = true }) {
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
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag("LeftOrRightCatScreen::TopAppBar::CurrentQuestionNumber"),
                    )
                    Text(
                        text = "Total Points: ${state.totalCorrect}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag("LeftOrRightCatScreen::TopAppBar::TotalCorrect"),
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
                    Text(
                        text = state.question,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(24.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
                AnimatedContent(
                    targetState = state.currentQuestionNumber,
                    transitionSpec = {
                        slideInHorizontally { it }.togetherWith(slideOutHorizontally { -it })
                    },
                    label = "ImageAnimation"
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        state.catImages.toList().forEachIndexed { index, catImage ->
                            Box(
                                modifier = Modifier
                                    .size(170.dp)
                                    .clickable { onCatImageClick(index) },
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(catImage.url),
                                    contentDescription = "Cat Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .testTag("LeftOrRightCatScreen::CatImage $index"),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = onSkipClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text("Skip Question")

                }
            }
        }
        // Landscape
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TopAppBar(
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
                        Row(
                            modifier = Modifier.weight(2f),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            state.catImages.toList().forEachIndexed { index, catImage ->
                                Box(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .padding(8.dp)
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
                    }
                }
            }
        }

        // Animation Light
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
