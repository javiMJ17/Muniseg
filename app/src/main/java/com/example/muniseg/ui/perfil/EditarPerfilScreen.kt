
package com.example.muniseg.ui.perfil

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.muniseg.repository.MunisegRepository


@Composable
fun EditarPerfilScreen(
    onVolverClick: () -> Unit
) {


    var nombre by remember { mutableStateOf(MunisegRepository.usuarioActual.value) }

    var correo by remember { mutableStateOf(MunisegRepository.correoActual.value) }

    var mensaje by remember { mutableStateOf("") }

    var nuevaContrasena by remember { mutableStateOf("") }

    var confirmarContrasena by remember { mutableStateOf("") }

    var cambiosGuardados by remember { mutableStateOf(false) }





    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // Encabezado celeste
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFAFC0F4)
        ) {
            Text(
                text = "Editar perfil",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF172E55),
                modifier = Modifier.padding(24.dp)
            )
        }

        // Formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
        }

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
                mensaje = when {

                    nombre.isBlank() || correo.isBlank() -> {
                        "Debes completar el nombre y correo"
                    }

                    nombre.trim().length < 3 -> {
                        "El nombre debe tener al menos 3 caracteres"
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> {
                        "Ingresa un correo electrónico válido"
                    }

                    (nuevaContrasena.isBlank() &&
                            confirmarContrasena.isNotBlank()) ||
                            (nuevaContrasena.isNotBlank() &&
                                    confirmarContrasena.isBlank()) -> {
                        "Debes completar ambos campos de contraseña"
                    }

                    nuevaContrasena.isNotEmpty() &&
                            nuevaContrasena.length < 8 -> {
                        "La contraseña debe tener al menos 8 caracteres"
                    }

                    nuevaContrasena != confirmarContrasena -> {
                        "Las contraseñas no coinciden"
                    }


                    else -> {
                        MunisegRepository.usuarioActual.value = nombre.trim()
                        MunisegRepository.correoActual.value = correo.trim()

                        cambiosGuardados = true

                        if (nuevaContrasena.isNotEmpty()) {
                            "Nombre y correo guardados. El cambio de contraseña está pendiente."
                        } else {
                            "Los cambios se guardaron correctamente"
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
            Text("Guardar cambios")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (cambiosGuardados) {

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { onVolverClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a Mi Perfil")
            }
        }

    }
}
