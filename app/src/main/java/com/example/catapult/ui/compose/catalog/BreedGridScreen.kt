package com.example.catapult.ui.compose.catalog

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.catalog.grid.BreedGridContract
import com.example.catapult.model.catalog.grid.BreedGridViewModel
import com.example.catapult.ui.compose.AppIconButton

// Navigation
fun NavGraphBuilder.breedImagesGrid(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
    enterTransition = { slideInHorizontally { it } },
    exitTransition = { scaleOut (targetScale = 0.75f) },
    popEnterTransition = { scaleIn(initialScale = 0.75f) },
    popExitTransition = { slideOutHorizontally { it } },
) { navBackStackEntry ->
    val breedId = navBackStackEntry.arguments?.getString("breedId")
        ?: throw IllegalStateException("breedId required")

    val breedGridViewModel = hiltViewModel<BreedGridViewModel>(navBackStackEntry)

    val onImageClick: (String) -> Unit = { imageId ->
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
            TopAppBar(
                title = { },
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
                        key = { _, image: UIBreedImage ->
                            image.id
                        },
                    ) { _, image: UIBreedImage ->
                        Card(
                            modifier = Modifier
                                .size(cellSize)
                                .clickable { onImageClick(image.id) },
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(image.url),
                                    contentDescription = "Breed Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}