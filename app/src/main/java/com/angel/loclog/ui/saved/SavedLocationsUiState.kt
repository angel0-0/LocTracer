package com.angel.loclog.ui.saved

import com.angel.loclog.data.local.LocationEntity

data class SavedLocationsUiState(
    val locations: List<LocationEntity> = emptyList(),
    val error: String? = null
)
