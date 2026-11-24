package com.example.muniseg.viewmodel

import com.example.muniseg.remote.IncidenciaApi
import com.example.muniseg.remote.NuevaIncidenciaApi
import com.example.muniseg.repository.IncidenciasRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IncidenciasViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `un error de conexion publica estado de error`() = runTest {

        println("INICIO: comprobar el manejo de errores de conexion")

        val viewModel = IncidenciasViewModel(FakeRepository())

        assertEquals(
            IncidenciasUiState.Cargando,
            viewModel.estado.value
        )

        advanceUntilIdle()

        assertEquals(
            IncidenciasUiState.Error,
            viewModel.estado.value
        )

        println("CORRECTO: el ViewModel reconoce el error de conexion")
    }

    // Repositorio simulado para realizar la prueba
    private class FakeRepository : IncidenciasRepository {

        override suspend fun obtenerIncidencias(): List<IncidenciaApi> {
            throw IOException("Sin conexion")
        }

        override suspend fun registrarIncidencia(
            incidencia: NuevaIncidenciaApi
        ): IncidenciaApi {
            error("Operacion no utilizada en esta prueba")
        }

        override suspend fun obtenerIncidenciaPorId(
            id: Int
        ): IncidenciaApi {
            error("Operacion no utilizada en esta prueba")
        }

        override suspend fun obtenerCategorias(): List<String> {
            return emptyList()
        }

        override suspend fun obtenerPrioridades(): List<String> {
            return emptyList()
        }

        override suspend fun obtenerEstados(): List<String> {
            return emptyList()
        }

        override suspend fun obtenerAlertas(): List<IncidenciaApi> {
            return emptyList()
        }
    }
}
