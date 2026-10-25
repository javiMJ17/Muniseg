package com.example.muniseg.report

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCase(onBackClick: () -> Unit){

    //Texto
    var descripcion by remember{mutableStateOf("")}
    var ubicacion by remember { mutableStateOf("")}
    //Categoria
    var expandedCategoria by remember {mutableStateOf(false)}
    val categorias = listOf ("Seguridad", "Aseo y Ornato", "Transito", "Otro")
    var selectedCategory by remember { mutableStateOf(categorias[0])}
    //Prioridad
    var expandedPrioridad by remember { mutableStateOf(false)}
    val prioridades = listOf("Baja", "Media", "Alta")
    var selectedPriority by remember {mutableStateOf(prioridades[1])}


    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Nuevo caso")},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ){
            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = {expandedCategoria = !expandedCategoria}
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Categoria")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = {expandedCategoria = false}
                ){
                    categorias.forEach { opcion -> DropdownMenuItem(
                        text = {Text(opcion)},
                        onClick = {
                            selectedCategory = opcion
                            expandedCategoria = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = {descripcion = it},
                label={Text("Descripción")},
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ubicacion,
                onValueChange = {ubicacion = it},
                label ={Text("Ubicación (referencial)")},
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandedPrioridad,
                onExpandedChange = {expandedPrioridad = !expandedPrioridad}
            ) {
                OutlinedTextField(
                    value = selectedPriority,
                    onValueChange ={},
                    readOnly = true,
                    label = {Text("Prioridad")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedPrioridad,
                    onDismissRequest = {expandedPrioridad = false}
                ){
                    prioridades.forEach { opcion -> DropdownMenuItem(
                        text = {Text(opcion)},
                        onClick = {
                            selectedPriority = opcion
                            expandedPrioridad = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {/*Accion*/},
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Enviar")
            }
        }
    }
}