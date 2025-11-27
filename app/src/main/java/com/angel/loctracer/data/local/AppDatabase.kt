package com.angel.loctracer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LocationEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE locations ADD COLUMN title TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new table with correct schema
        database.execSQL("""
            CREATE TABLE locations_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL,
                mapsLink TEXT NOT NULL,
                timestamp INTEGER NOT NULL
            )
        """.trimIndent())

        // Copy data from old table to new table, setting timestamp to 0 for old entries
        database.execSQL("""
            INSERT INTO locations_new (id, title, latitude, longitude, mapsLink, timestamp)
            SELECT id, title, latitude, longitude, mapsLink, 0 FROM locations
        """.trimIndent())

        // Drop the old table
        database.execSQL("DROP TABLE locations")

        // Rename new table to the original name
        database.execSQL("ALTER TABLE locations_new RENAME TO locations")
    }
}
