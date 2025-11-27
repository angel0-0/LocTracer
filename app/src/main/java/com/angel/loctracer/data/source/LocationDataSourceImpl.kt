package com.angel.loctracer.data.source

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationDataSourceImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationDataSource {

    @SuppressLint("MissingPermission")
    override fun fetchLocationUpdates(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3000)
            .setMaxUpdateDelayMillis(10000)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { trySend(it) }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null)

        awaitClose { fusedLocationProviderClient.removeLocationUpdates(callback) }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        continuation.resume(location)
                    } else {
                        continuation.resumeWithException(Exception("No se pudo obtener la ubicación."))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
}
