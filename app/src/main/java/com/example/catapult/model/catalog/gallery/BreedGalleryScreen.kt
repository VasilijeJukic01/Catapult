package com.example.catapult.model.catalog.gallery

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.compose.AppIconButton
import com.example.catapult.compose.ImagePreview

fun NavGraphBuilder.breedGalleryScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
) { navBackStackEntry ->
    val breedId = navBackStackEntry.arguments?.getString("breedId")
        ?: throw IllegalStateException("breedId required")
    val currentImage = navBackStackEntry.arguments?.getString("currentImage")
        ?: throw IllegalStateException("currentImage required")

    val breedGalleryViewModel = viewModel<BreedGalleryViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BreedGalleryViewModel(breedId = breedId, currentImage = currentImage) as T
            }
        }
    )

    val state = breedGalleryViewModel.state.collectAsState()

    BreedGalleryScreen(
        state = state.value,
        onClose = onClose,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreedGalleryScreen(
    state: BreedGalleryContract.BreedGalleryUiState,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            state.images.size
        }
    )

    LaunchedEffect(state.images, state.currentIndex) {
        if (state.images.isNotEmpty()) {
            pagerState.scrollToPage(state.currentIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                title = {}
            )
        },
        content = { paddingValues ->
            if (state.images.isNotEmpty()) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = { state.images[it].id }
                ) { pageIndex ->
                    val image = state.images[pageIndex]
                    ImagePreview(
                        modifier = Modifier,
                        image = image,
                    )
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No images.",
                )
            }
        },
    )
}
