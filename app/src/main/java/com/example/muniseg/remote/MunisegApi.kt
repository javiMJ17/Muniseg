
package com.example.muniseg.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Datos de una incidencia obtenida desde Xano
data class IncidenciaApi(
    val id: Int,
    val created_at: Long,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val ubicacion: String,
    val estado: String,
    val prioridad: String
)

// Datos que enviaremos para registrar una incidencia
data class NuevaIncidenciaApi(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val ubicacion: String,
    val estado: String = "Ingresado",
    val prioridad: String = "Media"
)

// Endpoints de nuestra API
interface MunisegApi {

    // GET: consultar todas las incidencias
    @GET("incidencias")
    suspend fun obtenerIncidencias(): List<IncidenciaApi>

    // POST: registrar una nueva incidencia
    @POST("incidencias")
    suspend fun registrarIncidencia(
        @Body incidencia: NuevaIncidenciaApi
    ): IncidenciaApi
}
