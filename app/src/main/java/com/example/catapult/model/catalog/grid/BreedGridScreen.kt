package com.example.catapult.model.catalog.grid

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.compose.AppIconButton
import com.example.catapult.compose.ImagePreview
import com.example.catapult.model.catalog.ViewBreedImage

fun NavGraphBuilder.breedImagesGrid(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->
    val breedId = navBackStackEntry.arguments?.getString("breedId")
        ?: throw IllegalStateException("breedId required")

    val breedGridViewModel = viewModel<BreedGridViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BreedGridViewModel(breedId = breedId) as T
            }
        }
    )

    val onImageClick: (String) -> Unit = { imageId ->
        Log.d("BreedImagesGrid", "onImageClick: breedId=$breedId, imageId=$imageId")
        navController.navigate("breeds/gallery/${breedId}?currentImage=$imageId")
    }

    BreedGridScreen(
        state = breedGridViewModel.state.collectAsState().value,
        onImageClick = onImageClick,
        onClose = onClose,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedGridScreen(
    state: BreedGridContract.BreedGridUiState,
    onImageClick: (breedId: String) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Images") },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier,
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    itemsIndexed(
                        items = state.images,
                        key = { _, image: ViewBreedImage ->
                            image.id
                        },
                    ) { _, image: ViewBreedImage ->
                        Card(
                            modifier = Modifier
                                .size(cellSize)
                                .clickable {
                                    onImageClick(image.id)
                                },
                        ) {
                            ImagePreview(
                                modifier = Modifier.fillMaxSize(),
                                image = image,
                            )
                        }
                    }
                }
            }
        },
    )
}