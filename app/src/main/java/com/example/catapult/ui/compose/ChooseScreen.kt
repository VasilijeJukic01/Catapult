package com.example.catapult.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.R
import com.example.catapult.ui.compose.user.UserDrawer
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*

// Navigation
fun NavGraphBuilder.chooseScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    ChooseScreen(navController = navController)
}

@Composable
fun ChooseScreen(navController: NavController) {
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            UserDrawer(
                drawerState = drawerState,
                onDrawerDestinationClick = {},
                navController = navController
            )
        },
        content = {
            val image = painterResource(id = R.drawable.background)

            Box(modifier = Modifier.fillMaxSize()) {
                // Background
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                // Drawer Icon
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier.padding(top = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(30.dp)
                    )
                }
                // Buttons
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 160.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Catalog
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { navController.navigate("breeds") }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.btn1),
                                contentDescription = "Catalog"
                            )
                        }
                        // Guess the Fact
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { navController.navigate("guessTheFact") }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.btn2),
                                contentDescription = "Quiz 1"
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Guess the Cat
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { navController.navigate("guessTheCat") }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.btn3),
                                contentDescription = "Quiz 2"
                            )
                        }
                        // Left or Right
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { navController.navigate("leftOrRight") }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.btn4),
                                contentDescription = "Quiz 3"
                            )
                        }
                    }
                }
                // Version
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "v 0.0.1",
                        fontSize = 18.sp
                    )
                }
            }
        }
    )
}
