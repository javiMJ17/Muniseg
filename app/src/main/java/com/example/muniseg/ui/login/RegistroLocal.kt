package com.example.muniseg.ui.login


data class UsuarioDemo(
    val nombre: String,
    val correo: String
)

object RegistroLocal {

    private val usuarios = mutableListOf<UsuarioDemo>()

    fun guardarUsuario(nombre: String, correo: String): Boolean {

        val correoLimpio = correo.trim().lowercase()

        if (usuarios.any { it.correo == correoLimpio }) {
            return false
        }

        usuarios.add(
            UsuarioDemo(nombre.trim(), correoLimpio)
        )

        return true
    }
}
