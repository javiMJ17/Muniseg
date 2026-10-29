package com.example.muniseg.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.muniseg.R
import com.example.muniseg.ui.navigation.AppBottomBar
import com.example.muniseg.ui.navigation.Pantalla
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(
    onReportClick: () -> Unit,
    onMenuClick: () -> Unit,
    onAlertasClick: () -> Unit,
    onNotificacionesClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
)

  {
    val context = LocalContext.current
    var permisoConcedido by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permisoConcedido = granted
        if (granted) {
            viewModel.fetchUserLocation(context)
        }
    }

    // Pedir permiso de ubicacion al iniciar la pantalla
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val defaultLocation = LatLng(-33.49936500787212, -70.61654033901539)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(viewModel.userLocation ?: defaultLocation, 15f)
    }

    // la vista del mapa cambia a la ubicación del ViewModel
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(viewModel.userLocation) {
        viewModel.userLocation?.let { latLng ->
            coroutineScope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(latLng, 16f),
                    durationMs = 1000
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "San Joaquín", fontWeight = FontWeight.Bold, color = Color.Black) },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo San Joaquín",
                        modifier = Modifier.size(48.dp).padding(end = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        bottomBar = {

            AppBottomBar(
                pantallaActual = Pantalla.Home.ruta,
                onReportClick = { /* Ya estamos en Inicio */ },
                onCasesClick = onAlertasClick,
                onMenuClick = onMenuClick,
                onNotificationsClick = onNotificacionesClick
            )

        }
    ) { innerPadding ->
        val lugar1 = LatLng(-33.497672632070476, -70.6126025410391)
        val lugar2 = LatLng(-33.50104607891704, -70.61707122623334)
        val lugar3 = LatLng(-33.49774554586376, -70.6178305190539)

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false),
                properties = MapProperties(isMyLocationEnabled = permisoConcedido)
            ) {
                MarkerComposable(
                    state = MarkerState(position = lugar1),
                    title = "Reporte de Seguridad"
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color.Gray,
                                shape = androidx.compose.foundation.shape.CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.LocalPolice,
                            contentDescription = "Seguridad",
                            tint = Color.Blue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                MarkerComposable(
                    state = MarkerState(position = lugar2),
                    title = "Reporte de Aseo"
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color.Gray,
                                shape = androidx.compose.foundation.shape.CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.CleaningServices,
                            contentDescription = "Aseo y Ornato",
                            tint = Color.Yellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Button(
                onClick = onReportClick,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(text = "Reportar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}