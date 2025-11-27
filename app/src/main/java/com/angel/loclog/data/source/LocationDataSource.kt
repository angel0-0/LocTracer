package com.angel.loclog.data.source

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    fun fetchLocationUpdates(): Flow<Location>
    suspend fun getCurrentLocation(): Location
}
