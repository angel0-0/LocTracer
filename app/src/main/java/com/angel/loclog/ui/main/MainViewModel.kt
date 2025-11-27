package com.angel.loclog.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angel.loclog.data.local.LocationEntity
import com.angel.loclog.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _locationSaved = Channel<Boolean>()
    val locationSaved = _locationSaved.receiveAsFlow()

    private val _error = Channel<String>()
    val error = _error.receiveAsFlow()

    fun saveCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val location = repository.getCurrentLocation()
                val locationCount = repository.countLocations()

                val newLocation = LocationEntity(
                    title = "Ubicación ${locationCount + 1}",
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timestamp = System.currentTimeMillis(),
                    mapsLink = "https://www.google.com/maps/search/?api=1&query=${location.latitude},${location.longitude}"
                )

                repository.saveLocation(newLocation)

                _locationSaved.send(true)

            } catch (e: Exception) {
                _error.send(e.message ?: "Error al guardar la ubicación")
            }
        }
    }
}
