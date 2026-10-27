package com.example.muniseg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muniseg.home.HomeScreen
import com.example.muniseg.login.LoginScreen
import com.example.muniseg.report.ReportCase
import com.example.muniseg.login.RegisterScreen


sealed class Pantalla(val ruta: String) {
    object Login : Pantalla("login_screen")
    object Home : Pantalla("home_screen")
    object NuevoCaso: Pantalla(("Nuevo_caso_screen"))
    object Registro : Pantalla("register_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Login.ruta
    ) {

        composable(Pantalla.Login.ruta) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Pantalla.Home.ruta) {
                        popUpTo(Pantalla.Login.ruta) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Pantalla.Registro.ruta)
                }
            )
        }


        composable(Pantalla.Registro.ruta) {
            RegisterScreen(
                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }




        composable(Pantalla.Home.ruta) {
            HomeScreen(
                onReportClick = {navController.navigate(Pantalla.NuevoCaso.ruta)}
            )
        }
        composable(Pantalla.NuevoCaso.ruta){
            ReportCase(
                onBackClick = {navController.popBackStack()},
                onSubmitClick = {/* Boton confirmar */}
            )
        }
    }
}