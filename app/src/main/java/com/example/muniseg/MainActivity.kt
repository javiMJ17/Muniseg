package com.example.muniseg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.muniseg.ui.navigation.AppNavigation
import com.example.muniseg.ui.theme.MuniSegTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuniSegTheme {
                AppNavigation()
            }
        }
    }
}