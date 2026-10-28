package com.example.muniseg.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.muniseg.ui.navigation.AppBottomBar
import com.example.muniseg.ui.navigation.Pantalla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onProfileClick: () -> Unit,
    onCasesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onReportClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Menú", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            AppBottomBar(
                pantallaActual = Pantalla.Menu.ruta,
                onReportClick = onReportClick,
                onCasesClick = { /* Vacío por ahora */ },
                onMenuClick = { /* Ya estás en Menú */ }
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Opciones Principales Superior
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Mi Perfil
                MenuItemRow(
                    icon = Icons.Default.Person,
                    title = "Mi Perfil",
                    subtitle = "Consulta y edita tus datos personales",
                    onClick = onProfileClick
                )

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                // 2. Mis Casos
                MenuItemRow(
                    icon = Icons.Default.Description,
                    title = "Mis Casos",
                    subtitle = "Revisa el historial de tus reportes",
                    onClick = onCasesClick
                )

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            }

            // Opción de Cerrar Sesión en la parte inferior
            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MenuItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Ir",
            tint = Color.Gray
        )
    }
}