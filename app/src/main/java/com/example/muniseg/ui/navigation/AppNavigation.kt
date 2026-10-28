package com.example.muniseg.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muniseg.home.HomeScreen
import com.example.muniseg.report.ReportCase
import com.example.muniseg.ui.login.RegisterScreen
import com.example.muniseg.ui.perfil.PerfilScreen
import com.example.muniseg.ui.login.LoginScreen
import com.example.muniseg.ui.perfil.EditarPerfilScreen


sealed class Pantalla(val ruta: String) {

    object Login : Pantalla("login_screen")

    object Home : Pantalla("home_screen")

    object NuevoCaso : Pantalla("Nuevo_caso_screen")

    object Registro : Pantalla("register_screen")

    object Perfil : Pantalla("perfil_screen")

    object EditarPerfil : Pantalla("editar_perfil_screen")
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
                onReportClick = { navController.navigate(Pantalla.NuevoCaso.ruta) },
                onProfileClick = { navController.navigate(Pantalla.Perfil.ruta) }
            )
        }
        composable(Pantalla.NuevoCaso.ruta) {
            ReportCase(
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { /* Boton confirmar */ }
            )
        }

        composable(Pantalla.Perfil.ruta) {
            PerfilScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    navController.navigate(Pantalla.Login.ruta) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                onEditClick = {
                    navController.navigate(Pantalla.EditarPerfil.ruta)
                }
            )
        }


        composable(Pantalla.EditarPerfil.ruta) {
            EditarPerfilScreen(
                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}