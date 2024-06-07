package com.example.catapult.ui.compose.quiz

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.example.catapult.model.quiz.guess_fact.GuessFactContract
import com.example.catapult.model.quiz.guess_fact.GuessFactViewModel
import com.example.catapult.model.quiz.guess_fact.GuessFactContract.GuessTheFactUiEvent
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessTheFactScreen(
    state: GuessFactContract.GuessTheFactState,
    guessFactViewModel: GuessFactViewModel,
    onFactOptionClick: (Int) -> Unit,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit,
    navController: NavController
) {
    val correctColor = Color.Green
    val incorrectColor = Color.Red

    LaunchedEffect(Unit) {
        guessFactViewModel.eventsFlow.collect { event ->
            when (event) {
                is GuessTheFactUiEvent.EndQuiz -> {
                    navController.navigate("quizEndScreen/${event.totalPoints}")
                }
                else -> Unit
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
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
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp),
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
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.options.size) { index ->
                    Button(
                        onClick = { onFactOptionClick(index) },
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        Text(
                            text = state.options[index].uppercase(Locale.ROOT),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
            Button(
                onClick = onSkipClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
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



fun NavGraphBuilder.guessTheFactScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val guessFactViewModel = viewModel<GuessFactViewModel>()

    val state by guessFactViewModel.state.collectAsState()

    GuessTheFactScreen(
        state = state,
        guessFactViewModel = guessFactViewModel,
        onFactOptionClick = { index ->
            guessFactViewModel.setEvent(GuessTheFactUiEvent.SelectFact(index))
        },
        onSkipClick = {
            guessFactViewModel.setEvent(GuessTheFactUiEvent.NextQuestion(false))
        },
        onBackClick = {
            navController.popBackStack()
        },
        navController = navController
    )


}
