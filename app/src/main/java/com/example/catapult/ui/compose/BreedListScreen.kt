package com.example.catapult.ui.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.catapult.R
import com.example.catapult.dummies.DataSample
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.list.BreedListContract.BreedListState
import com.example.catapult.model.catalog.list.BreedListContract.BreedListUiEvent
import com.example.catapult.model.catalog.list.BreedListViewModel
import com.example.catapult.ui.theme.*

@ExperimentalMaterial3Api
@Composable
fun BreedListScreen(
    state : BreedListState,
    eventPublisher: (BreedListUiEvent) -> Unit,
    onClick: (UIBreed) -> Unit
) {
    val logo: Painter = painterResource(id = R.drawable.logo_vector)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold (
        topBar = {
            CustomTopBar(
                logo = logo,
                state = state,
                eventPublisher = eventPublisher,
                keyboardController = keyboardController,
                focusManager = focusManager
            )
        },
        content = {
            BreedList(
                items = state.currentUIBreeds,
                padding = it,
                onClick = onClick
            )
            DisplayEmptyStateOrError(state)
        }
    )
}

// Components
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTopBar(
    logo: Painter,
    state: BreedListState,
    eventPublisher: (BreedListUiEvent) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    val searchText = remember { mutableStateOf(state.filter) }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "Catalog")
            },
            navigationIcon = {
                Image(
                    painter = logo,
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(48.dp)
                )
            }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchText.value,
            onValueChange = {
                searchText.value = it
                eventPublisher(BreedListUiEvent.SearchChanged(it))
            },
            label = { Text("Search") },
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            trailingIcon = {
                if (searchText.value.isNotEmpty()) {
                    IconButton(onClick = {
                        eventPublisher(BreedListUiEvent.SearchChanged(""))
                        searchText.value = ""
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear text")
                    }
                }
            }
        )
        Divider()
    }
}

@Composable
private fun BreedList(
    items: List<UIBreed>,
    padding: PaddingValues,
    onClick: (UIBreed) -> Unit
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(items) { breed ->
            BreedCard(
                uiBreed = breed,
                onClick = { onClick(breed) },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DisplayEmptyStateOrError(state: BreedListState) {
    if (state.currentUIBreeds.isEmpty()) {
        when (state.fetching) {
            true -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state.error == null) "No breeds found" else "Error: ${state.error}"
                    )
                }
            }
        }
    }
}

// Navigation
@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.breedsListScreen(
    route: String,
    navController: NavController,
) = composable (route = route) {

    val breedListViewModel = hiltViewModel<BreedListViewModel>()

    // Transforms state into state that can be observed by Compose
    val state by breedListViewModel.state.collectAsState()

    BreedListScreen(
        state = state,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onClick = { breed ->
            navController.navigate(route = "breeds/${breed.id}")
        },
    )
}

// Preview
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewCatListScreen() {
    CatalogTheme {
        BreedListScreen(
            state = BreedListState(UIBreeds = DataSample),
            eventPublisher = {},
            onClick = {},
        )
    }
}