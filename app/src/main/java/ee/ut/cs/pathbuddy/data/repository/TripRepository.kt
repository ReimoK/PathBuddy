package ee.ut.cs.pathbuddy.data.repository

import com.google.firebase.auth.FirebaseAuth
import ee.ut.cs.pathbuddy.data.local.TripDao
import ee.ut.cs.pathbuddy.data.trip.Trip
import ee.ut.cs.pathbuddy.data.trip.toDomain
import ee.ut.cs.pathbuddy.data.trip.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Repository for managing trip data.
 * This class abstracts the data source (TripDao) and provides a clean API for the rest of the app.
 *
 * @param tripDao The Data Access Object for trips.
 */
class TripRepository(private val tripDao: TripDao) {

    // Firebase authentication instance to identify the current user.
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Retrieves all trips and maps them from TripEntity to the domain Trip model.
     * @return A Flow emitting a list of all domain Trip objects.
     */
    fun getTrips(): Flow<List<Trip>> {
        val uid = auth.currentUser?.uid ?: return flowOf(emptyList())
        return tripDao.getTrips(uid).map { entities -> entities.map { it.toDomain() } }
    }

    /**
     * Retrieves a single trip by its ID and maps it to the domain Trip model.
     * @param id The ID of the trip to retrieve.
     * @return A Flow emitting the domain Trip object, or null if not found.
     */
    fun getTrip(id: Int): Flow<Trip?> {
        val uid = auth.currentUser?.uid ?: return flowOf(null)
        return tripDao.getTrip(id, uid).map { it?.toDomain() }
    }

    /**
     * Saves a trip to the database. It inserts a new trip if the ID is 0,
     * otherwise it updates the existing trip.
     * @param trip The domain Trip object to save.
     * @return The ID of the saved trip.
     */
    suspend fun saveTrip(trip: Trip): Int {
        val uid = auth.currentUser?.uid ?: return -1
        // If the trip ID is 0, it's a new trip, so we insert it.
        // Otherwise, it's an existing trip that needs to be updated.
        return if (trip.id == 0) {
            tripDao.insertTrip(trip.toEntity().copy(userId = uid)).toInt()
        } else {
            tripDao.updateTrip(trip.toEntity().copy(userId = uid))
            trip.id
        }
    }

    /**
     * Clears all trips from the local database.
     * This is useful when a user logs out to ensure that no data from the previous
     * user remains visible to the next one.
     */
    suspend fun clearAllTrips() {
        tripDao.clearAllTrips()
    }
}
