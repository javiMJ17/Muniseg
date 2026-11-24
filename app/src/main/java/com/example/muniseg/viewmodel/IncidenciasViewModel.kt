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

// Estados de la pantalla de incidencias
sealed interface IncidenciasUiState {
    data object Cargando : IncidenciasUiState
    data class Exito(val incidencias: List<IncidenciaApi>) : IncidenciasUiState
    data object Error : IncidenciasUiState
}

class IncidenciasViewModel(
    private val repository: IncidenciasRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<IncidenciasUiState>(IncidenciasUiState.Cargando)

    val estado = _estado.asStateFlow()

    init {
        cargarIncidencias()
    }

    // Obtener las incidencias desde Xano
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
        viewModelScope.launch {
            try {
                repository.registrarIncidencia(incidencia)
                cargarIncidencias()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _estado.value = IncidenciasUiState.Error
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
