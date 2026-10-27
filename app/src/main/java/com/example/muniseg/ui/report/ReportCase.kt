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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import android.content.pm.PackageManager
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest
import android.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCase(onBackClick: () -> Unit, onSubmitClick: () -> Unit){
    val context = LocalContext.current

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
            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = {expandedCategoria = !expandedCategoria}
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Categoría")},
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

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {onSubmitClick},
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Enviar")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}