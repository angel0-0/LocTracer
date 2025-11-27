package com.angel.loclog.data.repository

import android.location.Location
import com.angel.loclog.data.local.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getAllLocations(): Flow<List<LocationEntity>>

    suspend fun saveLocation(location: LocationEntity): Long

    suspend fun deleteLocation(locationId: Int)

    suspend fun getCurrentLocation(): Location

    suspend fun countLocations(): Int

    suspend fun updateLocation(location: LocationEntity)
}
