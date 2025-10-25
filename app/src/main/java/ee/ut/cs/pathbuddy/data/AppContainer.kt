package ee.ut.cs.pathbuddy.data

import android.content.Context
import androidx.room.Room
import ee.ut.cs.pathbuddy.data.local.PathBuddyDatabase
import ee.ut.cs.pathbuddy.data.profile.ProfileRepository
import ee.ut.cs.pathbuddy.data.profile.profileDataStore
import ee.ut.cs.pathbuddy.data.repository.TripRepository

/**
 * Defines the contract for the application's dependency container.
 * This interface provides access to all the repositories.
 */
interface AppContainer {
    val tripRepository: TripRepository
    val profileRepository: ProfileRepository
}

/**
 * The default implementation of AppContainer, responsible for creating and providing
 * instances of repositories and the database. This acts as a manual dependency injector.
 *
 * @param context The application context, used for initializing the database and DataStore.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    // Lazily initialize the database to ensure it's created only when first needed.
    private val database by lazy {
        Room.databaseBuilder(
            context,
            PathBuddyDatabase::class.java,
            "pathbuddy.db"
        ).fallbackToDestructiveMigration() // Handles schema changes by destroying and rebuilding the db.
            .build()
    }

    /**
     * Lazily provides a singleton instance of the TripRepository.
     */
    override val tripRepository: TripRepository by lazy {
        TripRepository(database.tripDao())
    }

    /**
     * Lazily provides a singleton instance of the ProfileRepository.
     */
    override val profileRepository: ProfileRepository by lazy {
        ProfileRepository(context.profileDataStore)
    }
}