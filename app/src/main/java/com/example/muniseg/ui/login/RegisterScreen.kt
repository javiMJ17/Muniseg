package com.example.muniseg.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.util.Patterns
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import com.example.muniseg.data.AppDatabase
import com.example.muniseg.data.UsuarioEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onVolverClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear cuenta",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF172E55)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF172E55)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFAFC0F4)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val correoLimpio = correo.trim().lowercase()

                    when {
                        nombre.isBlank() || correo.isBlank() ||
                                contrasena.isBlank() || confirmarContrasena.isBlank() -> {
                            mensaje = "Debes completar todos los campos"
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(correoLimpio).matches() -> {
                            mensaje = "Ingresa un correo electrónico válido"
                        }
                        contrasena.length < 8 -> {
                            mensaje = "La contraseña debe tener al menos 8 caracteres"
                        }
                        contrasena != confirmarContrasena -> {
                            mensaje = "Las contraseñas no coinciden"
                        }
                        else -> {
                            coroutineScope.launch {
                                try {
                                    val dao = AppDatabase.getDatabase(context).usuarioDao()

                                    if (dao.obtenerUsuarioPorCorreo(correoLimpio) != null) {
                                        mensaje = "Este correo ya está registrado"
                                    } else {
                                        dao.insertarUsuario(
                                            UsuarioEntity(
                                                nombre = nombre.trim(),
                                                correo = correoLimpio,
                                                contrasena = contrasena
                                            )
                                        )
                                        mensaje = ""
                                        mostrarConfirmacion = true
                                    }
                                } catch (e: Exception) {
                                    mensaje = "No se pudo crear la cuenta, intenta de nuevo"
                                }
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
                Text("Crear cuenta", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (mensaje.isNotEmpty()) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (mostrarConfirmacion) {
                AlertDialog(
                    onDismissRequest = {
                        mostrarConfirmacion = false
                    },
                    title = {
                        Text("Registro exitoso")
                    },
                    text = {
                        Text("Tu cuenta de prueba fue creada correctamente.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { onVolverClick() }
                        ) {
                            Text("Volver al Login")
                        }
                    }
                )
            }
        }
    }
}