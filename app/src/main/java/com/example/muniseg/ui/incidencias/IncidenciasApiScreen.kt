
package com.example.muniseg.ui.incidencias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.muniseg.remote.RetrofitClient
import com.example.muniseg.repository.IncidenciasRepositoryImpl
import com.example.muniseg.viewmodel.IncidenciasUiState
import com.example.muniseg.viewmodel.IncidenciasViewModel
import com.example.muniseg.viewmodel.DetalleIncidenciaState

@Composable
fun IncidenciasApiScreen(onVolverClick: () -> Unit) {

    val repository = remember {
        IncidenciasRepositoryImpl(RetrofitClient.crearApi())
    }

    val viewModel: IncidenciasViewModel = viewModel(
        factory = IncidenciasViewModel.Factory(repository)
    )

    val estado by viewModel.estado.collectAsState()
    val detalleEstado by viewModel.detalleEstado.collectAsState()

    var mostrarDetalle by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        Text(
            text = "Incidencias en línea",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.cargarIncidencias() }) {
            Text("Actualizar incidencias")
        }

        TextButton(onClick = onVolverClick) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val resultado = estado) {

            IncidenciasUiState.Cargando -> {
                CircularProgressIndicator()
            }

            IncidenciasUiState.Error -> {
                Text("Error al consultar la API de Xano")
            }

            is IncidenciasUiState.Exito -> {

                if (resultado.incidencias.isEmpty()) {
                    Text("No hay incidencias registradas")
                } else {
                    LazyColumn {
                        items(resultado.incidencias) { incidencia ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = incidencia.titulo,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text("Categoría: ${incidencia.categoria}")
                                    Text("Estado: ${incidencia.estado}")
                                    Text("Prioridad: ${incidencia.prioridad}")

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedButton(
                                        onClick = {
                                            viewModel.obtenerIncidenciaPorId(incidencia.id)
                                            mostrarDetalle = true
                                        },
                                        enabled = detalleEstado !=
                                                DetalleIncidenciaState.Cargando
                                    ) {
                                        Text("Ver detalle")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Mostrar el detalle obtenido desde Xano
    if (mostrarDetalle) {

        AlertDialog(
            onDismissRequest = {
                mostrarDetalle = false
            },
            title = {
                Text("Detalle de incidencia")
            },
            text = {
                when (val detalle = detalleEstado) {

                    DetalleIncidenciaState.Inicial -> {
                        Text("Consultando incidencia...")
                    }

                    DetalleIncidenciaState.Cargando -> {
                        CircularProgressIndicator()
                    }

                    DetalleIncidenciaState.Error -> {
                        Text("No se pudo obtener la incidencia")
                    }

                    is DetalleIncidenciaState.Exito -> {

                        val incidencia = detalle.incidencia

                        Column {
                            Text("ID: ${incidencia.id}")
                            Text("Título: ${incidencia.titulo}")
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
