package com.example.catapult.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catapult.R

@Composable
fun ChooseScreen(navController: NavController) {
    val image = painterResource(id = R.drawable.background)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 160.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                        .clickable { navController.navigate("breeds") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn1),
                        contentDescription = "Quiz 1"
                    )
                }
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                        .clickable { navController.navigate("guessTheFact") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn2),
                        contentDescription = "Quiz 2"
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                        .clickable { navController.navigate("guessTheCat") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn3),
                        contentDescription = "Quiz 3"
                    )
                }
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                        .clickable { navController.navigate("leftOrRight") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn4),
                        contentDescription = "Catalog"
                    )
                }
            }
        }

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