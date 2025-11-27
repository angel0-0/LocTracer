package com.angel.loctracer.ui.saved

import com.angel.loctracer.data.local.LocationEntity

data class SavedLocationsUiState(
    val locations: List<LocationEntity> = emptyList(),
    val error: String? = null
)
