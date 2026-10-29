package com.example.muniseg.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {

    // 1. Guardar un nuevo usuario en la base de datos
    // Si hay un conflicto con el correo, podemos ignorarlo o reemplazarlo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    // 2. Buscar un usuario por su correo y contraseña (ideal para el Login)
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun obtenerUsuarioPorCredenciales(correo: String, contrasena: String): UsuarioEntity?

    // 3. Verificar si un correo ya está registrado (evita duplicados al crear cuenta)
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity?
}