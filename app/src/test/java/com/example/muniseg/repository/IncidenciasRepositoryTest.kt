package com.example.muniseg.repository

import com.example.muniseg.remote.IncidenciaApi
import com.example.muniseg.remote.RetrofitClient
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IncidenciasRepositoryTest {

    private lateinit var servidor: MockWebServer
    private lateinit var repository: IncidenciasRepository

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()

        val api = RetrofitClient.crearApi(
            servidor.url("/").toString()
        )

        repository = IncidenciasRepositoryImpl(api)
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `consulta correctamente las incidencias`() = runTest {

        println("INICIO: comprobar la consulta de incidencias")

        servidor.enqueue(
            MockResponse().setBody(
                """
                [
                    {
                        "id": 1,
                        "created_at": 1795479461375,
                        "titulo": "Luminaria publica dañada",
                        "descripcion": "La luminaria no funciona",
                        "categoria": "Seguridad",
                        "ubicacion": "San Joaquin",
                        "estado": "Ingresado",
                        "prioridad": "Media"
                    }
                ]
                """.trimIndent()
            )
        )

        val resultado = repository.obtenerIncidencias()

        val incidenciaEsperada = IncidenciaApi(
            id = 1,
            created_at = 1795479461375L,
            titulo = "Luminaria publica dañada",
            descripcion = "La luminaria no funciona",
            categoria = "Seguridad",
            ubicacion = "San Joaquin",
            estado = "Ingresado",
            prioridad = "Media"
        )

        assertEquals(listOf(incidenciaEsperada), resultado)

        val solicitud = servidor.takeRequest()

        assertEquals("GET", solicitud.method)
        assertEquals("/incidencias", solicitud.path)

        println("CORRECTO: las incidencias se obtuvieron correctamente")
    }
}
