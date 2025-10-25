package ee.ut.cs.pathbuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room database for the application.
 * This class defines the database configuration and serves as the main access point
 * to the persisted data.
 */
@Database(
    entities = [TripEntity::class],
    version = 3,
    exportSchema = false
)
abstract class PathBuddyDatabase : RoomDatabase() {
    /**
     * Provides an instance of the TripDao to interact with the 'trips' table.
     * @return The Data Access Object for trips.
     */
    abstract fun tripDao(): TripDao
}