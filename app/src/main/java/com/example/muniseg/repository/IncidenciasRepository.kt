package com.example.muniseg.repository

import com.example.muniseg.remote.IncidenciaApi
import com.example.muniseg.remote.NuevaIncidenciaApi
import com.example.muniseg.remote.MunisegApi

// Operaciones que utilizaremos para gestionar incidencias
interface IncidenciasRepository {

    suspend fun obtenerIncidencias(): List<IncidenciaApi>

    suspend fun registrarIncidencia(
        incidencia: NuevaIncidenciaApi
    ): IncidenciaApi
}

// Conexion del repositorio con nuestra API de Xano
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
}
