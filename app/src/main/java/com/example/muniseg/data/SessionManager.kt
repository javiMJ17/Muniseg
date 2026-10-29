package com.example.muniseg.data

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("muniseg_session", Context.MODE_PRIVATE)

    fun guardarSesion(nombre: String, correo: String) {
        android.util.Log.d("SESION", "guardando sesion de $correo")
        prefs.edit()
            .putBoolean("logged", true)
            .putString("nombre", nombre)
            .putString("correo", correo)
            .apply()
    }

    fun estaLogueado(): Boolean = prefs.getBoolean("logged", false)

    fun obtenerNombre(): String = prefs.getString("nombre", "") ?: ""

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}