package com.angel.loclog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val mapsLink: String,
    val timestamp: Long
)
