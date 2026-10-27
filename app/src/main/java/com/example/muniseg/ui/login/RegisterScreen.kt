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

@Composable

fun RegisterScreen(
    onVolverClick: () -> Unit ){

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrarConfirmacion by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)


    ) {

        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { onVolverClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio de sesión")
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
                    Text(
                        "Tu cuenta de prueba fue creada correctamente."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onVolverClick()
                        }
                    ) {
                        Text("Volver al Login")
                    }
                }
            )
        }

    }

}
