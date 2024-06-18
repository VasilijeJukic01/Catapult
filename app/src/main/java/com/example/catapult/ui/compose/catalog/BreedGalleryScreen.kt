package com.example.catapult.ui.compose.catalog

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.model.catalog.gallery.BreedGalleryContract
import com.example.catapult.model.catalog.gallery.BreedGalleryViewModel
import com.example.catapult.ui.compose.AppIconButton
import com.example.catapult.ui.compose.ImagePreview

// Navigation
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
    val breedGalleryViewModel = hiltViewModel<BreedGalleryViewModel>(navBackStackEntry)
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
        pageCount = { state.images.size }
    )

    LaunchedEffect(state.images, state.currentIndex) {
        if (state.images.isNotEmpty()) {
            pagerState.scrollToPage(state.currentIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {},

                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                })
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
