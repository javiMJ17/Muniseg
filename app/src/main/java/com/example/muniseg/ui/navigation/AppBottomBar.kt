package com.example.muniseg.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppBottomBar(
    pantallaActual: String,
    onReportClick: () -> Unit,
    onCasesClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = pantallaActual == Pantalla.Home.ruta,
            onClick = onReportClick,
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Reportes") },
            label = { Text("Reportes") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onCasesClick,
            icon = { Icon(Icons.Default.Campaign, contentDescription = "Alertas") },
            label = { Text("Alertas") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            label = { Text("Notificaciones") }
        )
        NavigationBarItem(
            selected = pantallaActual == Pantalla.Menu.ruta, // <--- SE MARCA SEGÚN LA RUTA
            onClick = onMenuClick,
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menú") },
            label = { Text("Menú") }
        )
    }
}