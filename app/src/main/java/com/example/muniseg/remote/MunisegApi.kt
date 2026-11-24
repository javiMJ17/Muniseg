package com.example.muniseg.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Datos recibidos desde Xano
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

// Datos para registrar una incidencia
data class NuevaIncidenciaApi(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val ubicacion: String,
    val estado: String = "Ingresado",
    val prioridad: String = "Media"
)

interface MunisegApi {

    // Endpoint 1: consultar incidencias
    @GET("incidencias")
    suspend fun obtenerIncidencias(): List<IncidenciaApi>

    // Endpoint 2: registrar incidencia
    @POST("incidencias")
    suspend fun registrarIncidencia(
        @Body incidencia: NuevaIncidenciaApi
    ): IncidenciaApi

    // Endpoint 3: consultar incidencia por ID
    @GET("incidencias/{incidencias_id}")
    suspend fun obtenerIncidenciaPorId(
        @Path("incidencias_id") id: Int
    ): IncidenciaApi

    // Endpoint 4: consultar categorias
    @GET("categorias")
    suspend fun obtenerCategorias(): List<String>

    // Endpoint 5: consultar prioridades
    @GET("prioridades")
    suspend fun obtenerPrioridades(): List<String>

    // Endpoint 6: consultar estados
    @GET("estados")
    suspend fun obtenerEstados(): List<String>

    // Endpoint 7: consultar alertas activas
    @GET("alertas")
    suspend fun obtenerAlertas(): List<IncidenciaApi>
}
