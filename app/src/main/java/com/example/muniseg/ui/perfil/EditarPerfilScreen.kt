package com.example.muniseg.ui.perfil

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.muniseg.data.AppDatabase
import com.example.muniseg.data.SessionManager
import com.example.muniseg.repository.MunisegRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    onVolverClick: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val scope = rememberCoroutineScope()

    val database = AppDatabase.getDatabase(context)
    val usuarioDao = database.usuarioDao()

    var nombre by remember { mutableStateOf(sessionManager.obtenerNombre().ifEmpty { MunisegRepository.usuarioActual.value }) }
    var correo by remember { mutableStateOf(sessionManager.obtenerCorreo().ifEmpty { MunisegRepository.correoActual.value }) }

    var mensajeError by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var cambiosGuardados by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Nombre completo
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Correo electrónico
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nueva contraseña
            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                label = { Text("Nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmar contraseña
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón guardar cambios
            Button(
                onClick = {
                    when {
                        nombre.isBlank() || correo.isBlank() -> {
                            mensajeError = "Debes completar el nombre y correo"
                        }
                        nombre.trim().length < 3 -> {
                            mensajeError = "El nombre debe tener al menos 3 caracteres"
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> {
                            mensajeError = "Ingresa un correo electrónico válido"
                        }
                        (nuevaContrasena.isBlank() && confirmarContrasena.isNotBlank()) ||
                                (nuevaContrasena.isNotBlank() && confirmarContrasena.isBlank()) -> {
                            mensajeError = "Debes completar ambos campos de contraseña"
                        }
                        nuevaContrasena.isNotEmpty() && nuevaContrasena.length < 8 -> {
                            mensajeError = "La contraseña debe tener al menos 8 caracteres"
                        }
                        nuevaContrasena != confirmarContrasena -> {
                            mensajeError = "Las contraseñas no coinciden"
                        }
                        else -> {
                            mensajeError = ""
                            scope.launch {
                                MunisegRepository.usuarioActual.value = nombre.trim()
                                MunisegRepository.correoActual.value = correo.trim()
                                sessionManager.guardarSesion(nombre.trim(), correo.trim())

                                val correoAnterior = sessionManager.obtenerCorreo()
                                val usuarioExistente = usuarioDao.obtenerUsuarioPorCorreo(correoAnterior)

                                if (usuarioExistente != null) {
                                    val usuarioActualizado = usuarioExistente.copy(
                                        nombre = nombre.trim(),
                                        correo = correo.trim(),
                                        contrasena = if (nuevaContrasena.isNotEmpty()) nuevaContrasena else usuarioExistente.contrasena
                                    )
                                    usuarioDao.insertarUsuario(usuarioActualizado)
                                }

                                // Activamos el AlertDialog de éxito
                                cambiosGuardados = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAFC0F4),
                    contentColor = Color(0xFF172E55)
                )
            ) {
                Text("Guardar cambios", fontWeight = FontWeight.Bold)
            }

            // Muestra errores de validación si existen
            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // AlertDialog que se despliega al guardar exitosamente los cambios
            if (cambiosGuardados) {
                AlertDialog(
                    onDismissRequest = { },
                    title = {
                        Text("¡Cambios Guardados!")
                    },
                    text = {
                        Text("¡Los cambios se guardaron correctamente!")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { onVolverClick() }
                        ) {
                            Text("Volver a Mi Perfil")
                        }
                    }
                )
            }
        }
    }
}