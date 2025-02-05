package com.example.catapult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.catapult.navigation.AppNavigation
import com.example.catapult.ui.theme.CatalogTheme
import dagger.hilt.android.AndroidEntryPoint

// Entry point for injection
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogTheme {
                AppNavigation()
            }
        }
    }
}