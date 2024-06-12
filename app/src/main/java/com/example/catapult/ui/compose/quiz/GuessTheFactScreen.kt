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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catapult.model.quiz.guess_fact.GuessFactContract
import com.example.catapult.model.quiz.guess_fact.GuessFactViewModel
import com.example.catapult.model.quiz.guess_fact.GuessFactContract.GuessTheFactUiEvent
import java.util.Locale

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessTheFactScreen(
    state: GuessFactContract.GuessTheFactState,
    onFactOptionClick: (Int) -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val correctColor = Color.Green
    val incorrectColor = Color.Red

    val orientation = LocalConfiguration.current.orientation

    Surface(modifier = Modifier.fillMaxSize()) {
        // Portrait
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = { Text("Menu") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                    label = "ImageAnimation"
                ) {
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(state.catImage.url),
                            contentDescription = "Cat Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                AnimatedContent(
                    targetState = state.currentQuestionNumber,
                    transitionSpec = {
                        slideInHorizontally { it }.togetherWith(slideOutHorizontally { -it })
                    },
                    label = "OptionsAnimation"
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy((-20).dp),
                    ) {
                        items(state.options.size) { index ->
                            Button(
                                onClick = { onFactOptionClick(index) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .width(60.dp)
                                    .height(60.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = state.options[index].uppercase(Locale.ROOT),
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onSkipClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(all = 40.dp),
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
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(80.dp)
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
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .size(250.dp)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(state.catImage.url),
                                contentDescription = "Cat Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(state.options.size) { index ->
                                Button(
                                    onClick = { onFactOptionClick(index) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                        .width(80.dp)
                                        .height(40.dp)
                                ) {
                                    Text(
                                        text = state.options[index].uppercase(Locale.ROOT),
                                        style = MaterialTheme.typography.titleSmall,
                                        textAlign = TextAlign.Center
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
fun NavGraphBuilder.guessTheFactScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val guessFactViewModel = hiltViewModel<GuessFactViewModel>()

    val state by guessFactViewModel.state.collectAsState()

    if (state.quizEnded) {
        navController.navigate("quizEndScreen/${state.totalPoints}")
    } else {
        GuessTheFactScreen(
            state = state,
            onFactOptionClick = { index ->
                guessFactViewModel.setEvent(GuessTheFactUiEvent.SelectFact(index))
            },
            onSkipClick = {
                guessFactViewModel.setEvent(GuessTheFactUiEvent.NextQuestion(false))
            },
            onBackClick = {
                navController.popBackStack()
            },
        )
    }
}
