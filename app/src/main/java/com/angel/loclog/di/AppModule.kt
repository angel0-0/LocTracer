package com.angel.loclog.di

import android.content.Context
import androidx.room.Room
import com.angel.loclog.data.local.AppDatabase
import com.angel.loclog.data.local.LocationDao
import com.angel.loclog.data.repository.LocationRepository
import com.angel.loclog.data.repository.LocationRepositoryImpl
import com.angel.loclog.data.source.LocationDataSource
import com.angel.loclog.data.source.LocationDataSourceImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindLocationDataSource(
        locationDataSourceImpl: LocationDataSourceImpl
    ): LocationDataSource

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "locations_db_v5" // Renamed DB to force recreation
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        @Singleton
        fun provideLocationDao(database: AppDatabase): LocationDao {
            return database.locationDao()
        }

        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }
    }
}
