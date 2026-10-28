package com.example.muniseg.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import com.example.muniseg.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MunisegRepository {
    var rolActual = mutableStateOf(RolUsuario.VECINO)
    var usuarioActual = mutableStateOf("vecino_demo")

    var correoActual = mutableStateOf("vecino@ejemplo.com")



    val sectoresDisponibles = listOf(
        SectorReferencial("Cuadrante Norte - Sector Referencial 1", LatLng(-33.4976726, -70.6126025)),
        SectorReferencial("Cuadrante Sur - Plaza Referencial 2", LatLng(-33.5010460, -70.6170712)),
        SectorReferencial("Cuadrante Poniente - Eje Peatonal 3", LatLng(-33.4977455, -70.6178305)),
        SectorReferencial("Cuadrante Central - Intersección Segura 4", LatLng(-33.4993650, -70.6165403))
    )

    val categoriasDisponibles = listOf(
        "Luminaria pública dañada",
        "Ruidos molestos / Incivilidad",
        "Vehículo abandonado en vía",
        "Microbasural o escombros",
        "Situación sospechosa en sector"
    )

    val listaIncidencias = mutableStateListOf(
        Incidencia(
            id = "CASO-101",
            titulo = "Luminaria apagada en cruce peatonal",
            categoria = "Luminaria pública dañada",
            descripcion = "Poste sin luz desde hace dos días en esquina referencial (Dato sintético).",
            sector = sectoresDisponibles[0],
            fechaCreacion = "23/10/2026 16:30",
            reportadoPor = "Vecino Anónimo #104",
            inspectorAsignado = "Patrulla Preventiva 02",
            estado = EstadoCaso.DERIVADO,
            trazabilidad = listOf(
                EventoTrazabilidad("23/10/2026 16:30", RolUsuario.VECINO, "Incidencia reportada con ubicación referencial.", EstadoCaso.INGRESADO),
                EventoTrazabilidad("23/10/2026 16:45", RolUsuario.OPERADOR, "Caso revisado y derivado a Patrulla Preventiva 02.", EstadoCaso.DERIVADO)
            )
        ),
        Incidencia(
            id = "CASO-102",
            titulo = "Escombros bloqueando vereda",
            categoria = "Microbasural o escombros",
            descripcion = "Acumulación de ramas y desechos que impide el paso peatonal.",
            sector = sectoresDisponibles[1],
            fechaCreacion = "23/10/2026 17:10",
            reportadoPor = "Vecino Anónimo #209",
            estado = EstadoCaso.INGRESADO,
            trazabilidad = listOf(
                EventoTrazabilidad("23/10/2026 17:10", RolUsuario.VECINO, "Ingreso de requerimiento comunitario.", EstadoCaso.INGRESADO)
            )
        ),
        Incidencia(
            id = "CASO-103",
            titulo = "Vehículo sin patente estacionado por semanas",
            categoria = "Vehículo abandonado en vía",
            descripcion = "Auto abandonado frente a área verde referencial. Se verificó y retiró con grúa municipal.",
            sector = sectoresDisponibles[2],
            fechaCreacion = "22/10/2026 11:00",
            reportadoPor = "Vecino Anónimo #104",
            inspectorAsignado = "Inspector Móvil 01",
            estado = EstadoCaso.CERRADO,
            trazabilidad = listOf(
                EventoTrazabilidad("22/10/2026 11:00", RolUsuario.VECINO, "Reporte inicial ingresado.", EstadoCaso.INGRESADO),
                EventoTrazabilidad("22/10/2026 12:15", RolUsuario.OPERADOR, "Asignado a Inspector Móvil 01 para inspección ocular.", EstadoCaso.DERIVADO),
                EventoTrazabilidad("22/10/2026 16:00", RolUsuario.INSPECTOR, "Inspección realizada en terreno y notificación cursada.", EstadoCaso.EN_TERRENO),
                EventoTrazabilidad("23/10/2026 09:30", RolUsuario.INSPECTOR, "Situación resuelta y vía despejada. Se cierra requerimiento.", EstadoCaso.CERRADO)
            )
        )
    )

    fun agregarIncidencia(titulo: String, categoria: String, descripcion: String, sector: SectorReferencial) {
        val ahora = obtenerHora()
        val nuevoId = "CASO-${104 + listaIncidencias.size}"
        val nueva = Incidencia(
            id = nuevoId,
            titulo = titulo,
            categoria = categoria,
            descripcion = descripcion,
            sector = sector,
            fechaCreacion = ahora,
            reportadoPor = rolActual.value.aliasAnonimo,
            estado = EstadoCaso.INGRESADO,
            trazabilidad = listOf(
                EventoTrazabilidad(ahora, RolUsuario.VECINO, "Requerimiento ingresado con ubicación referencial anonimizada.", EstadoCaso.INGRESADO)
            )
        )
        listaIncidencias.add(0, nueva)
    }

    fun derivarCaso(idCaso: String, inspector: String, nota: String) {
        val index = listaIncidencias.indexOfFirst { it.id == idCaso }
        if (index != -1) {
            val actual = listaIncidencias[index]
            val nuevoEvento = EventoTrazabilidad(
                fechaHora = obtenerHora(),
                rolAutor = RolUsuario.OPERADOR,
                detalle = "Derivado a $inspector. Instrucción: $nota",
                estadoResultante = EstadoCaso.DERIVADO
            )
            listaIncidencias[index] = actual.copy(
                inspectorAsignado = inspector,
                estado = EstadoCaso.DERIVADO,
                trazabilidad = actual.trazabilidad + nuevoEvento
            )
        }
    }

    fun registrarAccionInspector(idCaso: String, accion: String, cerrar: Boolean) {
        val index = listaIncidencias.indexOfFirst { it.id == idCaso }
        if (index != -1) {
            val actual = listaIncidencias[index]
            val nuevoEstado = if (cerrar) EstadoCaso.CERRADO else EstadoCaso.EN_TERRENO
            val nuevoEvento = EventoTrazabilidad(
                fechaHora = obtenerHora(),
                rolAutor = RolUsuario.INSPECTOR,
                detalle = accion,
                estadoResultante = nuevoEstado
            )
            listaIncidencias[index] = actual.copy(
                estado = nuevoEstado,
                trazabilidad = actual.trazabilidad + nuevoEvento
            )
        }
    }

    private fun obtenerHora(): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    }
}