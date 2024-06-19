package com.example.catapult.ui.compose.leaderboard

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.model.leaderboard.LeaderboardViewModel
import com.example.catapult.model.leaderboard.LeaderboardContract.*
import com.example.catapult.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextAlign

// Navigation
fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController
) = composable(
    route = route,
    enterTransition = { slideInHorizontally { it } },
    exitTransition = { scaleOut (targetScale = 0.75f) },
    popEnterTransition = { scaleIn(initialScale = 0.75f) },
    popExitTransition = { slideOutHorizontally { it } },
) {
    val viewModel = hiltViewModel<LeaderboardViewModel>()
    val state by viewModel.state.collectAsState()

    LeaderboardScreen(
        state = state,
        eventPublisher = { viewModel.setEvent(it) },
        onBackClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
    eventPublisher: (LeaderboardUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = "Back",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                // Text
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Leaderboard",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(
                        "Category 1",
                        "Category 2",
                        "Category 3"
                    ).forEachIndexed { index, category ->
                        SuggestionChip(
                            label = {
                                Text(
                                    text = category,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.Transparent
                            ),
                            onClick = { eventPublisher(LeaderboardUiEvent.SelectCategory(index + 1)) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Items
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Pos",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Nickname",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.weight(2f)
                            )
                            Text(
                                text = "Result",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Games",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End,
                            )
                        }
                    }
                    items(state.leaderboard) { item ->
                        val color = when (item.position) {
                            1 -> leaderboardCol1
                            2 -> leaderboardCol2
                            3 -> leaderboardCol3
                            in 4..10 -> leaderboardCol4
                            else -> leaderboardCol5
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = color
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.position.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = item.nickname,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    modifier = Modifier.weight(2f)
                                )
                                Text(
                                    text = String.format("%.2f", item.result),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = item.totalGamesSubmitted.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
