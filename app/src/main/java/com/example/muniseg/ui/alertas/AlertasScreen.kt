package com.example.muniseg.ui.alertas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.muniseg.model.EstadoCaso
import com.example.muniseg.model.Incidencia
import com.example.muniseg.repository.MunisegRepository
import com.example.muniseg.ui.navigation.AppBottomBar
import com.example.muniseg.ui.navigation.Pantalla




@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AlertasScreen(
    onCasoClick: (String) -> Unit,
    onInicioClick: () -> Unit,
    onNotificacionesClick: () -> Unit,
    onMenuClick: () -> Unit
)

  {
    var soloActivas by remember { mutableStateOf(true) }

    val alertas = MunisegRepository.listaIncidencias.filter {
        !soloActivas || it.estado != EstadoCaso.CERRADO
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
                onCasesClick = { },
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
                text = "Información de prueba. No corresponde a alertas municipales en tiempo real.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = soloActivas,
                    onClick = { soloActivas = true },
                    label = { Text("Activas") }
                )

                FilterChip(
                    selected = !soloActivas,
                    onClick = { soloActivas = false },
                    label = { Text("Todas") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: ${alertas.size}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (alertas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay alertas para mostrar")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(alertas, key = { it.id }) { incidencia ->

                        TarjetaAlerta(
                            incidencia = incidencia,
                            onClick = {
                                onCasoClick(incidencia.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaAlerta(
    incidencia: Incidencia,
    onClick: () -> Unit
) {
    val (colorFondo, colorTexto) = when (incidencia.estado) {
        EstadoCaso.INGRESADO ->
            Pair(Color(0xFFC8E6C9), Color(0xFF2E7D32))

        EstadoCaso.DERIVADO ->
            Pair(Color(0xFFFFECB3), Color(0xFFF57F17))

        EstadoCaso.EN_TERRENO ->
            Pair(Color(0xFFFFCDD2), Color(0xFFC62828))

        EstadoCaso.CERRADO ->
            Pair(Color(0xFFBBDEFB), Color(0xFF1565C0))
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
                style = MaterialTheme.typography.bodySmall,
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
                    text = incidencia.estado.etiqueta,
                    color = colorTexto,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Text(
                text = incidencia.sector.nombre,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = incidencia.fechaCreacion,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
