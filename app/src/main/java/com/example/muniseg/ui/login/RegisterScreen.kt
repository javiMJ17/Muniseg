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
                    mensaje = when {
                        nombre.isBlank() ||
                                correo.isBlank() ||
                                contrasena.isBlank() ||
                                confirmarContrasena.isBlank() -> {
                            "Debes completar todos los campos"
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> {
                            "Ingresa un correo electrónico válido"
                        }

                        contrasena.length < 8 -> {
                            "La contraseña debe tener al menos 8 caracteres"
                        }

                        contrasena != confirmarContrasena -> {
                            "Las contraseñas no coinciden"
                        }

                        else -> {
                            val registrado = RegistroLocal.guardarUsuario(
                                nombre,
                                correo
                            )

                            if (registrado) {
                                mostrarConfirmacion = true
                                ""
                            } else {
                                "Este correo ya está registrado"
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