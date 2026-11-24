package com.example.muniseg.repository

import com.example.muniseg.remote.IncidenciaApi
import com.example.muniseg.remote.NuevaIncidenciaApi
import com.example.muniseg.remote.MunisegApi

interface IncidenciasRepository {

    suspend fun obtenerIncidencias(): List<IncidenciaApi>

    suspend fun registrarIncidencia(
        incidencia: NuevaIncidenciaApi
    ): IncidenciaApi

    suspend fun obtenerIncidenciaPorId(id: Int): IncidenciaApi

    suspend fun obtenerCategorias(): List<String>

    suspend fun obtenerPrioridades(): List<String>

    suspend fun obtenerEstados(): List<String>

    suspend fun obtenerAlertas(): List<IncidenciaApi>
}

// Implementacion del repositorio conectado a Xano
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

    override suspend fun obtenerIncidenciaPorId(id: Int): IncidenciaApi {
        return api.obtenerIncidenciaPorId(id)
    }

    override suspend fun obtenerCategorias(): List<String> {
        return api.obtenerCategorias()
    }

    override suspend fun obtenerPrioridades(): List<String> {
        return api.obtenerPrioridades()
    }

    override suspend fun obtenerEstados(): List<String> {
        return api.obtenerEstados()
    }

    override suspend fun obtenerAlertas(): List<IncidenciaApi> {
        return api.obtenerAlertas()
    }
}
