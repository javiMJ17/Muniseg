package com.example.muniseg.model


import com.google.android.gms.maps.model.LatLng

enum class RolUsuario(val titulo: String, val aliasAnonimo: String) {
    VECINO("Vecino / Ciudadano", "Vecino Anónimo #104"),
    OPERADOR("Operador Municipal", "Operador Central San Joaquín"),
    INSPECTOR("Inspector Municipal", "Inspector Patrulla Preventiva 02")
}

enum class EstadoCaso(val etiqueta: String) {
    INGRESADO("Ingresado"),
    DERIVADO("Derivado a Inspector"),
    EN_TERRENO("En Gestión en Terreno"),
    CERRADO("Cerrado / Resuelto")
}

data class EventoTrazabilidad(
    val fechaHora: String,
    val rolAutor: RolUsuario,
    val detalle: String,
    val estadoResultante: EstadoCaso
)

data class SectorReferencial(
    val nombre: String,
    val coordenadas: LatLng
)

data class Incidencia(
    val id: String,
    val titulo: String,
    val categoria: String,
    val descripcion: String,
    val sector: SectorReferencial,
    val fechaCreacion: String,
    val reportadoPor: String,
    val inspectorAsignado: String? = null,
    val estado: EstadoCaso = EstadoCaso.INGRESADO,
    val trazabilidad: List<EventoTrazabilidad> = emptyList()
)