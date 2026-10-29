package com.example.muniseg.ui.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.muniseg.ui.navigation.AppBottomBar
import com.example.muniseg.ui.navigation.Pantalla



@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun NotificacionesScreen(
    onVolverClick: () -> Unit,
    onInicioClick: () -> Unit,
    onAlertasClick: () -> Unit,
    onMenuClick: () -> Unit
)

 {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF172E55)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF172E55)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFAFC0F4)
                )
            )

        },
        bottomBar = {
            AppBottomBar(
                pantallaActual = Pantalla.Notificaciones.ruta,
                onReportClick = onInicioClick,
                onCasesClick = onAlertasClick,
                onMenuClick = onMenuClick,
                onNotificationsClick = { }
            )
        }
    ) { paddingValues ->


    Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    modifier = Modifier.size(72.dp),
                    tint = Color(0xFF172E55)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Sin notificaciones",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF172E55)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Por ahora no tienes notificaciones para mostrar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Esta sección es informativa y no recibe avisos en tiempo real.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
