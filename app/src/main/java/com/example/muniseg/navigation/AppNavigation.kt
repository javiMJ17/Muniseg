package com.example.muniseg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muniseg.home.HomeScreen
import com.example.muniseg.login.LoginScreen

sealed class Pantalla(val ruta: String) {
    object Login : Pantalla("login_screen")
    object Home : Pantalla("home_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Login.ruta
    ) {
        composable(Pantalla.Login.ruta) {
            // Aquí llamas a tu pantalla de Login existente y le pasas la acción al botón ingresar
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Pantalla.Home.ruta) {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                }
            )
        }
        composable(Pantalla.Home.ruta) {
            HomeScreen()
        }
    }
}