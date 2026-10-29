package com.example.muniseg.report

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import android.content.pm.PackageManager
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import com.example.muniseg.model.SectorReferencial
import com.example.muniseg.repository.MunisegRepository
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCase(onBackClick: () -> Unit, onSubmitClick: () -> Unit){
    val context = LocalContext.current

    //Texto
    var descripcion by remember{mutableStateOf("")}
    var ubicacion by remember { mutableStateOf("")}

    //Categoria
    var expandedCategoria by remember {mutableStateOf(false)}
    val categorias = listOf ("Seguridad", "Aseo y Ornato", "Tránsito", "Otro")
    var selectedCategory by remember { mutableStateOf(categorias[0])}

    //Prioridad
    var expandedPrioridad by remember { mutableStateOf(false)}
    val prioridades = listOf("Baja", "Media", "Alta")
    var selectedPriority by remember {mutableStateOf(prioridades[1])}

    //Permiso camara
    var hasCameraPermission by remember{
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    //Camara
    var imageBitmap by remember {mutableStateOf<Bitmap?>(null)}
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(), onResult = { bitmap ->
            if (bitmap != null){
                imageBitmap = bitmap
            }
        }
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            hasCameraPermission = isGranted
            if (isGranted){
                cameraLauncher.launch(null)
            }
        }
    )

    //Validacion
    val isFormValid = descripcion.isNotBlank() && ubicacion.isNotBlank()
    var showErrors by remember {mutableStateOf(false)}


//Barra de título

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Nuevo caso",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                )},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",
                        tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){

// Selector categoría

            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = {expandedCategoria = !expandedCategoria}
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Categoría")},
                    leadingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    imageVector = when (selectedCategory){
                                        "Seguridad" -> androidx.compose.material.icons.Icons.Default.LocalPolice
                                        "Aseo y Ornato" -> androidx.compose.material.icons.Icons.Default.CleaningServices
                                        "Tránsito" -> androidx.compose.material.icons.Icons.Default.DirectionsCar
                                        else -> androidx.compose.material.icons.Icons.Default.MoreVert
                                    },
                                    contentDescription = "Iconos categoria",
                                    tint = when(selectedCategory){
                                        "Seguridad" -> Color.Blue
                                        "Aseo y Ornato" -> Color.Yellow
                                        "Tránsito" -> Color.Red
                                        else -> Color.LightGray
                                    },
                                    modifier = Modifier.size(18.dp)

                                )
                            }
                        }
                    },
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
                        leadingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                color = Color.Gray,
                                                shape = androidx.compose.foundation.shape.CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Icon(
                                            imageVector = when (opcion){
                                                "Seguridad" -> androidx.compose.material.icons.Icons.Default.LocalPolice
                                                "Aseo y Ornato" -> androidx.compose.material.icons.Icons.Default.CleaningServices
                                                "Tránsito" -> androidx.compose.material.icons.Icons.Default.DirectionsCar
                                                else -> androidx.compose.material.icons.Icons.Default.MoreVert
                                            },
                                            contentDescription = null,
                                            tint = when(opcion){
                                                "Seguridad" -> Color.Blue
                                                "Aseo y Ornato" -> Color.Yellow
                                                "Tránsito" -> Color.Red
                                                else -> Color.LightGray
                                            },
                                            modifier = Modifier.size(18.dp)

                                        )
                                    }
                                }
                        },
                        onClick = {
                            selectedCategory = opcion
                            expandedCategoria = false
                        })
                    }
                }
            }

//Espacio descripción

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    if (it.isNotBlank()) showErrors = false},
                label={Text("Descripción")},
                isError = showErrors && descripcion.isBlank(),
                supportingText = {
                    if(showErrors && descripcion.isBlank()){
                        Text("Este campo es obligatorio.", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )


//Espacio ubicación

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ubicacion,
                onValueChange = {
                    ubicacion = it
                    if(it.isNotBlank()) showErrors = false
                },
                label ={Text("Ubicación (referencial)")},
                isError = showErrors && ubicacion.isBlank(),
                supportingText = {
                    if(showErrors && ubicacion.isBlank()){
                        Text("Este campo es obligatorio.", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

//Selector prioridad

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
                    leadingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = when(selectedPriority){
                                        "Alta" -> Color.Red
                                        "Media" -> Color.Yellow
                                        "Baja" -> Color.Green
                                        else -> Color.Gray
                                    },
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
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
                        leadingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            color = Color.Gray,
                                            shape = androidx.compose.foundation.shape.CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = when(opcion){
                                            "Alta" -> Color.Red
                                            "Media" -> Color.Yellow
                                            "Baja" -> Color.Green
                                            else -> Color.Gray
                                        },
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            selectedPriority = opcion
                            expandedPrioridad = false
                        })
                    }
                }
            }

// Espacio foto

            Spacer(modifier = Modifier.height(16.dp))

            Text("Evidencia fotográfica (opcional)", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable{
                        if(imageBitmap == null){
                            if (hasCameraPermission){
                                cameraLauncher.launch(null)
                            } else{
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }

                    },
                contentAlignment = Alignment.Center
            ){
                if (imageBitmap == null){
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Icon(Icons.Default.Add, contentDescription = "Tomar foto")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Toca para abrir la cámara")
                    }
                }
                else{

                    Box(modifier = Modifier.fillMaxWidth()){
                        Image(
                            bitmap = imageBitmap!!.asImageBitmap(),
                            contentDescription = "Foto tomada",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )

                        IconButton(
                            onClick = {imageBitmap = null},
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(0.6f), shape = androidx.compose.foundation.shape.CircleShape)
                                .size(36.dp)
                        ){
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar foto",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }



                }
            }

//Boton Enviar

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (descripcion.isNotBlank() && ubicacion.isNotBlank()){
                        MunisegRepository.agregarIncidencia(
                            titulo = "$selectedCategory (Prioridad: $selectedPriority)",
                            categoria = selectedCategory,
                            descripcion = descripcion,
                            sector = SectorReferencial(ubicacion, LatLng(-33.499365, -70.616540)),
                            foto = imageBitmap
                        )
                        onSubmitClick()
                    } else{
                        showErrors = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Enviar")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

