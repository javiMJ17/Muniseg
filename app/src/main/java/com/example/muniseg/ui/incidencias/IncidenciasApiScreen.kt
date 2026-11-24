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

@Composable
fun IncidenciasApiScreen(onVolverClick: () -> Unit) {

    val repository = remember {
        IncidenciasRepositoryImpl(RetrofitClient.crearApi())
    }

    val viewModel: IncidenciasViewModel = viewModel(
        factory = IncidenciasViewModel.Factory(repository)
    )

    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Incidencias desde Xano",
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
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = incidencia.titulo,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text("Descripcion: ${incidencia.descripcion}")
                                    Text("Categoria: ${incidencia.categoria}")
                                    Text("Ubicacion: ${incidencia.ubicacion}")
                                    Text("Estado: ${incidencia.estado}")
                                    Text("Prioridad: ${incidencia.prioridad}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
