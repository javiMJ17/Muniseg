
package com.example.muniseg.ui.alertas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.muniseg.remote.IncidenciaApi
import com.example.muniseg.remote.RetrofitClient
import com.example.muniseg.repository.IncidenciasRepositoryImpl
import com.example.muniseg.viewmodel.IncidenciasViewModel
import com.example.muniseg.viewmodel.IncidenciasUiState
import com.example.muniseg.viewmodel.DetalleIncidenciaState
import com.example.muniseg.ui.navigation.AppBottomBar
import com.example.muniseg.ui.navigation.Pantalla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertasScreen(
    onInicioClick: () -> Unit,
    onNotificacionesClick: () -> Unit,
    onMenuClick: () -> Unit
) {

    val repository = remember {
        IncidenciasRepositoryImpl(RetrofitClient.crearApi())
    }

    val viewModel: IncidenciasViewModel = viewModel(
        factory = IncidenciasViewModel.Factory(repository)
    )

    val alertasEstado by viewModel.alertasEstado.collectAsState()
    val incidenciasEstado by viewModel.estado.collectAsState()
    val detalleEstado by viewModel.detalleEstado.collectAsState()

    var soloActivas by remember { mutableStateOf(true) }
    var mostrarDetalle by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarAlertas()
    }

    val resultado = if (soloActivas) {
        alertasEstado
    } else {
        incidenciasEstado
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Alertas",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF172E55)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFAFC0F4)
                )
            )
        },
        bottomBar = {
            AppBottomBar(
                pantallaActual = Pantalla.Alertas.ruta,
                onReportClick = onInicioClick,
                onCasesClick = {},
                onMenuClick = onMenuClick,
                onNotificationsClick = onNotificacionesClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Incidencias registradas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Información de prueba obtenida desde Xano.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = soloActivas,
                    onClick = {
                        soloActivas = true
                        viewModel.cargarAlertas()
                    },
                    label = { Text("Activas") }
                )

                FilterChip(
                    selected = !soloActivas,
                    onClick = {
                        soloActivas = false
                        viewModel.cargarIncidencias()
                    },
                    label = { Text("Todas") }
                )
            }

            TextButton(
                onClick = {
                    if (soloActivas) {
                        viewModel.cargarAlertas()
                    } else {
                        viewModel.cargarIncidencias()
                    }
                }
            ) {
                Text("Actualizar alertas")
            }

            when (resultado) {

                IncidenciasUiState.Cargando -> {
                    CircularProgressIndicator()
                }

                IncidenciasUiState.Error -> {
                    Text("No se pudieron cargar las alertas")
                }

                is IncidenciasUiState.Exito -> {

                    Text(
                        text = "Total: ${resultado.incidencias.size}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (resultado.incidencias.isEmpty()) {
                        Text("No hay alertas para mostrar")
                    } else {

                        LazyColumn(
                            verticalArrangement =
                                Arrangement.spacedBy(12.dp),
                            contentPadding =
                                PaddingValues(bottom = 16.dp)
                        ) {

                            items(
                                resultado.incidencias,
                                key = { it.id }
                            ) { incidencia ->

                                TarjetaAlerta(
                                    incidencia = incidencia,
                                    onClick = {
                                        if (detalleEstado !=
                                            DetalleIncidenciaState.Cargando
                                        ) {
                                            viewModel.obtenerIncidenciaPorId(
                                                incidencia.id
                                            )
                                            mostrarDetalle = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDetalle) {
        AlertDialog(
            onDismissRequest = { mostrarDetalle = false },
            title = { Text("Detalle de alerta") },
            text = {
                when (val detalle = detalleEstado) {

                    DetalleIncidenciaState.Inicial,
                    DetalleIncidenciaState.Cargando -> {
                        CircularProgressIndicator()
                    }

                    DetalleIncidenciaState.Error -> {
                        Text("No se pudo obtener el detalle")
                    }

                    is DetalleIncidenciaState.Exito -> {
                        val incidencia = detalle.incidencia

                        Column {
                            Text(incidencia.titulo)
                            Text("Descripción: ${incidencia.descripcion}")
                            Text("Categoría: ${incidencia.categoria}")
                            Text("Ubicación: ${incidencia.ubicacion}")
                            Text("Estado: ${incidencia.estado}")
                            Text("Prioridad: ${incidencia.prioridad}")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { mostrarDetalle = false }
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun TarjetaAlerta(
    incidencia: IncidenciaApi,
    onClick: () -> Unit
) {

    val (colorFondo, colorTexto) = when (incidencia.estado) {

        "Ingresado" ->
            Pair(Color(0xFFC8E6C9), Color(0xFF2E7D32))

        "Derivado a Inspector" ->
            Pair(Color(0xFFFFECB3), Color(0xFFF57F17))

        "En Terreno" ->
            Pair(Color(0xFFFFCDD2), Color(0xFFC62828))

        "Cerrado / Resuelto" ->
            Pair(Color(0xFFBBDEFB), Color(0xFF1565C0))

        else ->
            Pair(Color.LightGray, Color.DarkGray)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F9FD)
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Alerta",
                    tint = Color(0xFF172E55)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = incidencia.titulo,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Ver detalle",
                    tint = Color.Gray
                )
            }

            Text(
                text = incidencia.categoria,
                color = Color.Gray
            )

            Box(
                modifier = Modifier
                    .background(
                        colorFondo,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = incidencia.estado,
                    color = colorTexto
                )
            }

            Text("Ubicación: ${incidencia.ubicacion}")
            Text("Prioridad: ${incidencia.prioridad}")
        }
    }
}
