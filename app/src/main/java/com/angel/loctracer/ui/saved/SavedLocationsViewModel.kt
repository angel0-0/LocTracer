package com.angel.loctracer.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.loctracer.data.local.LocationEntity
import com.angel.loctracer.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedLocationsUiState())
    val uiState: StateFlow<SavedLocationsUiState> = _uiState.asStateFlow()

    init {
        loadSavedLocations()
    }

    private fun loadSavedLocations() {
        locationRepository.getAllLocations()
            .onEach { locations ->
                _uiState.update { it.copy(locations = locations, error = null) } // Clear error on new data
            }
            .catch { e ->
                _uiState.update { it.copy(error = e.localizedMessage ?: "Error al cargar ubicaciones") }
            }
            .launchIn(viewModelScope)
    }

    fun deleteLocation(locationId: Int) {
        viewModelScope.launch(Dispatchers.IO) { // Run on a background thread
            try {
                locationRepository.deleteLocation(locationId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Error al eliminar ubicación") }
            }
        }
    }

    fun updateLocationTitle(locationToUpdate: LocationEntity, newTitle: String) {
        viewModelScope.launch(Dispatchers.IO) { // Run on a background thread
            try {
                val updatedLocation = locationToUpdate.copy(title = newTitle)
                locationRepository.updateLocation(updatedLocation)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage ?: "Error al actualizar el título") }
            }
        }
    }
}
