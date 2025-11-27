package com.angel.loclog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity): Long

    @Query("SELECT * FROM locations ORDER BY timestamp DESC")
    fun getAllLocations(): kotlinx.coroutines.flow.Flow<List<LocationEntity>>

    @Query("DELETE FROM locations WHERE id = :locationId")
    suspend fun deleteLocation(locationId: Int)

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun countLocations(): Int

    @Update
    suspend fun updateLocation(location: LocationEntity)
}
