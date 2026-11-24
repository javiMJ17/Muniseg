
package com.example.muniseg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.muniseg.remote.IncidenciaApi
import com.example.muniseg.remote.NuevaIncidenciaApi
import com.example.muniseg.repository.IncidenciasRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estados de la consulta de incidencias
sealed interface IncidenciasUiState {
    data object Cargando : IncidenciasUiState
    data class Exito(val incidencias: List<IncidenciaApi>) : IncidenciasUiState
    data object Error : IncidenciasUiState
}

// Estados del registro de incidencias
sealed interface RegistroIncidenciaState {
    data object Inicial : RegistroIncidenciaState
    data object Guardando : RegistroIncidenciaState
    data object Exito : RegistroIncidenciaState
    data object Error : RegistroIncidenciaState
}

// Estados del detalle de una incidencia
sealed interface DetalleIncidenciaState {
    data object Inicial : DetalleIncidenciaState
    data object Cargando : DetalleIncidenciaState
    data class Exito(val incidencia: IncidenciaApi) : DetalleIncidenciaState
    data object Error : DetalleIncidenciaState
}

class IncidenciasViewModel(
    private val repository: IncidenciasRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<IncidenciasUiState>(IncidenciasUiState.Cargando)

    val estado = _estado.asStateFlow()

    private val _registroEstado =
        MutableStateFlow<RegistroIncidenciaState>(RegistroIncidenciaState.Inicial)

    val registroEstado = _registroEstado.asStateFlow()

    private val _detalleEstado =
        MutableStateFlow<DetalleIncidenciaState>(DetalleIncidenciaState.Inicial)

    val detalleEstado = _detalleEstado.asStateFlow()

    init {
        cargarIncidencias()
    }

    // Obtener todas las incidencias desde Xano
    fun cargarIncidencias() {
        _estado.value = IncidenciasUiState.Cargando

        viewModelScope.launch {
            try {
                val incidencias = repository.obtenerIncidencias()
                _estado.value = IncidenciasUiState.Exito(incidencias)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _estado.value = IncidenciasUiState.Error
            }
        }
    }

    // Registrar una incidencia en Xano
    fun registrarIncidencia(incidencia: NuevaIncidenciaApi) {

        if (_registroEstado.value == RegistroIncidenciaState.Guardando) return

        _registroEstado.value = RegistroIncidenciaState.Guardando

        viewModelScope.launch {
            try {
                repository.registrarIncidencia(incidencia)
                _registroEstado.value = RegistroIncidenciaState.Exito
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _registroEstado.value = RegistroIncidenciaState.Error
            }
        }
    }

    // Consultar una incidencia especifica desde Xano
    fun obtenerIncidenciaPorId(id: Int) {

        if (_detalleEstado.value == DetalleIncidenciaState.Cargando) return

        _detalleEstado.value = DetalleIncidenciaState.Cargando

        viewModelScope.launch {
            try {
                val incidencia = repository.obtenerIncidenciaPorId(id)
                _detalleEstado.value = DetalleIncidenciaState.Exito(incidencia)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _detalleEstado.value = DetalleIncidenciaState.Error
            }
        }
    }

    // Permite crear el ViewModel con nuestro repositorio
    class Factory(
        private val repository: IncidenciasRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(IncidenciasViewModel::class.java))

            @Suppress("UNCHECKED_CAST")
            return IncidenciasViewModel(repository) as T
        }
    }
}
