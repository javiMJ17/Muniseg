package com.example.muniseg.ui.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muniseg.model.EstadoCaso
import com.example.muniseg.model.Incidencia
import com.example.muniseg.repository.MunisegRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CaseDetailScreen(casoId: String, onBackClick: () -> Unit) {
    val incidencia = MunisegRepository.listaIncidencias.find { it.id == casoId }
    var nuevaObservacion by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember {mutableStateOf(false)}

    if (incidencia == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("Caso no encontrado") }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle ${incidencia.id}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick ={showDeleteDialog = true }
                    ){
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar caso",
                            tint = Color.Red

                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            incidencia.foto?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Evidencia fotografica",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }



            DatoFila(label = "Titulo: ", valor = incidencia.titulo)
            DatoFila(label = "Categoria: ", valor = incidencia.categoria)
            DatoFila(label = "Descripcion: ", valor = incidencia.descripcion)
            DatoFila(label = "Ubicacion: ", valor = incidencia.sector.nombre)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Estado ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                val (bgColor, textColor) = when (incidencia.estado) {
                    EstadoCaso.INGRESADO -> Pair(Color(0xFFC8E6C9), Color(0xFF2E7D32))
                    EstadoCaso.DERIVADO -> Pair(Color(0xFFFFECB3), Color(0xFFF57F17))
                    EstadoCaso.EN_TERRENO -> Pair(Color(0xFFFFCDD2), Color(0xFFC62828))
                    EstadoCaso.CERRADO -> Pair(Color(0xFFBBDEFB), Color(0xFF1565C0))
                }
                Box(
                    modifier = Modifier.background(
                        color = bgColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = incidencia.estado.etiqueta,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Text(
                text = "Historial",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            incidencia.trazabilidad.forEach { evento ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "${evento.fechaHora} - ${evento.rolAutor.titulo}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = evento.detalle, fontSize = 14.sp)
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1BEE7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar observacion", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nueva obsevacion") },
            text = {
                OutlinedTextField(
                    value = nuevaObservacion,
                    onValueChange = { nuevaObservacion = it },
                    label = { Text("Escribe una observacion") },
                    modifier = Modifier.fillMaxWidth()
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        if (nuevaObservacion.isNotBlank()) {
                            MunisegRepository.registrarAccionInspector(
                                idCaso = incidencia.id,
                                accion = nuevaObservacion,
                                cerrar = false
                            )
                            nuevaObservacion = ""
                            showDialog = false
                        }
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if(showDeleteDialog){
        AlertDialog(
            onDismissRequest = {showDeleteDialog = false},
            title ={Text("Estas seguro?")},
            text = {Text("Se eliminara el caso")},
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        MunisegRepository.eliminarCaso(incidencia.id)
                        onBackClick()
                    }
                ){
                    Text("Eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {showDeleteDialog= false}){
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun DatoFila(label: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = valor, fontSize = 15.sp, color = Color.DarkGray, modifier = Modifier.padding(start = 4.dp))
    }
}

