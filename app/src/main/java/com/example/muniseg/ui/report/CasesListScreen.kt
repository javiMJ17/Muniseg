package com.example.muniseg.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muniseg.model.EstadoCaso
import com.example.muniseg.model.Incidencia
import com.example.muniseg.repository.MunisegRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CasesListScreen(onBackClick: () -> Unit, onCaseClick: (String) -> Unit){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Mis Casos", fontWeight = FontWeight.Bold, color = Color.Black)},
                navigationIcon = {
                    IconButton(onClick = onBackClick){
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)

            )
        }
    ) {paddingValues ->
        if (MunisegRepository.listaIncidencias.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Text("Aun no tienes casos", color = Color.Gray, fontSize = 16.sp)
            }
        }else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ){
                items(MunisegRepository.listaIncidencias){incidecia ->
                    IncidenciaItemRow(incidecia, onClick = {onCaseClick(incidecia.id)})
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun IncidenciaItemRow(incidencia: Incidencia, onClick:() -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(modifier = Modifier.weight(1f)) {
            Text(text = incidencia.id, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text= incidencia.categoria, fontSize = 14.sp, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val (bgColor, textColor) = when (incidencia.estado) {
                EstadoCaso.INGRESADO -> Pair(Color(0xFFC8E6C9), Color(0xFF2E7D32))
                EstadoCaso.DERIVADO -> Pair(Color(0xFFFFECB3), Color(0xFFF57F17))
                EstadoCaso.EN_TERRENO -> Pair(Color(0xFFFFCDD2), Color(0xFFC62828))
                EstadoCaso.CERRADO -> Pair(Color(0xFFBBDEFB), Color(0xFF1565C0))
            }
            Box(
                modifier = Modifier
                    .background(color = bgColor, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ){
                Text(text= incidencia.estado.etiqueta, color = textColor, fontWeight = FontWeight.Bold, fontSize = 13.sp )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Ver", tint = Color.Gray)

        }
    }
}