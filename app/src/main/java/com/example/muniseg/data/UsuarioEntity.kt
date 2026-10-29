package com.example.muniseg.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,               // Un identificador único automático para cada usuario
    val nombre: String,            // El nombre completo que ingresó en el registro
    val correo: String,            // El correo electrónico (que usará para iniciar sesión)
    val contrasena: String         // La contraseña para validarla al momento del Login
)