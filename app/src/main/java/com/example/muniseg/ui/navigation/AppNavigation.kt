package com.example.muniseg.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muniseg.home.HomeScreen
import com.example.muniseg.report.ReportCase
import com.example.muniseg.ui.home.MenuScreen
import com.example.muniseg.ui.login.RegisterScreen
import com.example.muniseg.ui.perfil.PerfilScreen
import com.example.muniseg.ui.login.LoginScreen
import com.example.muniseg.ui.perfil.EditarPerfilScreen
import com.example.muniseg.ui.report.ConfirmationScreen
import com.example.muniseg.ui.report.CasesListScreen
import com.example.muniseg.ui.report.CaseDetailScreen
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.muniseg.data.SessionManager
import com.example.muniseg.repository.MunisegRepository


sealed class Pantalla(val ruta: String) {

    object Login : Pantalla("login_screen")

    object Home : Pantalla("home_screen")

    object NuevoCaso : Pantalla("Nuevo_caso_screen")

    object Registro : Pantalla("register_screen")

    object Perfil : Pantalla("perfil_screen")

    object EditarPerfil : Pantalla("editar_perfil_screen")
    object Confirmacion : Pantalla("confirmacion_screen")

    object Menu : Pantalla("menu_screen")

    object MisCasos : Pantalla("mis_casos_screen")

    object  DetalleCaso: Pantalla("detalle_caso_screen/{casoId}"){
        fun crearRuta(id: String) = "detalle_caso_screen/$id"
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    // Se calcula una sola vez al abrir la app
    val destinoInicial = remember {
        if (session.estaLogueado()) {
            MunisegRepository.usuarioActual.value = session.obtenerNombre()
            Pantalla.Home.ruta
        } else {
            Pantalla.Login.ruta
        }
    }

    // Una sola función para cerrar sesión, usada en Menu y Perfil
    val cerrarSesion = {
        session.cerrarSesion()
        MunisegRepository.usuarioActual.value = ""
        navController.navigate(Pantalla.Login.ruta) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = destinoInicial
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
                onMenuClick = { navController.navigate(Pantalla.Menu.ruta) }
            )
        }
        composable(Pantalla.Menu.ruta) {
            MenuScreen(
                onProfileClick = { navController.navigate(Pantalla.Perfil.ruta) },
                onCasesClick = {navController.navigate(Pantalla.MisCasos.ruta)},
                onLogoutClick = cerrarSesion,
                onReportClick = {
                    navController.navigate(Pantalla.Home.ruta) {
                        popUpTo(Pantalla.Home.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Pantalla.NuevoCaso.ruta) {
            ReportCase(
                onBackClick = { navController.popBackStack() },
                onSubmitClick = {navController.navigate(Pantalla.Confirmacion.ruta)}
            )
        }
        composable (Pantalla.Confirmacion.ruta){
            ConfirmationScreen(
                onNavigateHome = {
                    navController.navigate(Pantalla.Home.ruta){
                        popUpTo(0){inclusive = true}
                    }
                },
                onNavigateMisCasos = {
                    navController.navigate(Pantalla.MisCasos.ruta) {
                        popUpTo(Pantalla.Home.ruta)
                    }
                }
            )
        }

        composable(Pantalla.Perfil.ruta) {
            PerfilScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = cerrarSesion,
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

        composable(Pantalla.MisCasos.ruta){
            CasesListScreen(
                onBackClick = {navController.popBackStack()},
                onCaseClick = {idCaso -> navController.navigate(Pantalla.DetalleCaso.crearRuta(idCaso))}
            )
        }

        composable(Pantalla.DetalleCaso.ruta) {backStackEntry ->
            val casoId = backStackEntry.arguments?.getString("casoId") ?:""
            CaseDetailScreen(
                casoId = casoId,
                onBackClick = {navController.popBackStack()}
            )
        }
    }
}