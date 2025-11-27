package com.angel.loclog.data.repository

import android.location.Location
import com.angel.loclog.data.local.LocationDao
import com.angel.loclog.data.local.LocationEntity
import com.angel.loclog.data.source.LocationDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val dao: LocationDao,
    private val dataSource: LocationDataSource
) : LocationRepository {

    override fun getAllLocations(): Flow<List<LocationEntity>> {
        return dao.getAllLocations()
    }

    override suspend fun saveLocation(location: LocationEntity): Long {
        return dao.insertLocation(location)
    }

    override suspend fun deleteLocation(locationId: Int) {
        dao.deleteLocation(locationId)
    }

    override suspend fun getCurrentLocation(): Location {
        return dataSource.getCurrentLocation()
    }

    override suspend fun countLocations(): Int {
        return dao.countLocations()
    }

    override suspend fun updateLocation(location: LocationEntity) {
        dao.updateLocation(location)
    }
}
