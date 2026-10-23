package com.example.muniseg.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "San Joaquín",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                actions = {
                    IconButton(onClick = { /* Acción para abrir el perfil */ }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Reportes") },
                    label = { Text("Reportes") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Description, contentDescription = "Mis Casos") },
                    label = { Text("Mis Casos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
                    label = { Text("Notificaciones") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Menú") },
                    label = { Text("Menú") }
                )
            }
        }
    ) { innerPadding ->
        val miUbicacion = LatLng(-33.49936500787212, -70.61654033901539)

        val lugar1 = LatLng(-33.497672632070476, -70.6126025410391)
        val lugar2 = LatLng(-33.50104607891704, -70.61707122623334)
        val lugar3 = LatLng(-33.49774554586376, -70.6178305190539)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(miUbicacion, 15f)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. MAPA DE FONDO (Ocupa toda la pantalla disponible)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                Marker(
                    state = MarkerState(position = lugar1),
                    title = "Lugar 1"
                )
                Marker(
                    state = MarkerState(position = lugar2),
                    title = "Lugar 2"
                )
                Marker(
                    state = MarkerState(position = lugar3),
                    title = "Lugar 3"
                )
            }

            // BOTÓN FLOTANTE Reportar
            Button(
                onClick = { /* Acción para reportar */ },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Reportar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}