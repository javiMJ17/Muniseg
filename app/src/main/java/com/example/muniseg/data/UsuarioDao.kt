package com.example.muniseg.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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

    // 4. Observar los datos del usuario en tiempo real usando Flow (ideal para la pantalla de Perfil)
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    fun obtenerUsuarioPorCorreoFlow(correo: String): Flow<UsuarioEntity?>

    // 5. Actualizar los datos del usuario al editar su perfil
    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)
}