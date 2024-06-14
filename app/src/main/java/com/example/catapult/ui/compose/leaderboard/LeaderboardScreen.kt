package com.example.catapult.ui.compose.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.model.leaderboard.LeaderboardViewModel
import com.example.catapult.model.leaderboard.LeaderboardContract.*
import com.example.catapult.ui.theme.leaderboardCol1
import com.example.catapult.ui.theme.leaderboardCol2
import com.example.catapult.ui.theme.leaderboardCol3
import com.example.catapult.ui.theme.leaderboardCol4
import com.example.catapult.ui.theme.leaderboardCol5

// Navigation
fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController
) = composable(route = route) {
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
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            title = { Text("Leaderboard") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Text
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               Text(
                    text = "Leaderboard",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp),
                )
            }
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Category 1", "Category 2", "Category 3").forEachIndexed { index, category ->
                    SuggestionChip(
                        label = {
                            Text(
                                text = category,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
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
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = item.nickname,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = item.result.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}
