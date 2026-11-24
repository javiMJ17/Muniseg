
package com.example.muniseg.repository

import com.example.muniseg.remote.IncidenciaApi
import com.example.muniseg.remote.NuevaIncidenciaApi
import com.example.muniseg.remote.MunisegApi

// Operaciones para gestionar incidencias
interface IncidenciasRepository {

    suspend fun obtenerIncidencias(): List<IncidenciaApi>

    suspend fun registrarIncidencia(
        incidencia: NuevaIncidenciaApi
    ): IncidenciaApi

    suspend fun obtenerIncidenciaPorId(
        id: Int
    ): IncidenciaApi
}

// Conexion del repositorio con la API de Xano
class IncidenciasRepositoryImpl(
    private val api: MunisegApi
) : IncidenciasRepository {

    override suspend fun obtenerIncidencias(): List<IncidenciaApi> {
        return api.obtenerIncidencias()
    }

    override suspend fun registrarIncidencia(
        incidencia: NuevaIncidenciaApi
    ): IncidenciaApi {
        return api.registrarIncidencia(incidencia)
    }

    override suspend fun obtenerIncidenciaPorId(
        id: Int
    ): IncidenciaApi {
        return api.obtenerIncidenciaPorId(id)
    }
}
