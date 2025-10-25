package ee.ut.cs.pathbuddy.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the 'trips' table.
 * This interface defines the methods for interacting with the trip data in the database.
 */
@Dao
interface TripDao {

    /**
     * Retrieves all trips from the database for a specific user, ordered by their start date.
     * @param userId The UID of the user whose trips are being retrieved.
     * @return A Flow emitting a list of TripEntity objects belonging to the given user.
     */
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY startDate ASC")
    fun getTrips(userId: String): Flow<List<TripEntity>>

    /**
     * Retrieves a single trip by its unique ID for a specific user.
     * @param id The ID of the trip to retrieve.
     * @param userId The UID of the user who owns the trip.
     * @return A Flow emitting the corresponding TripEntity, or null if not found.
     */
    @Query("SELECT * FROM trips WHERE id = :id AND userId = :userId")
    fun getTrip(id: Int, userId: String): Flow<TripEntity?>

    /**
     * Inserts a new trip into the database. If a trip with the same primary key
     * already exists, it will be replaced.
     * @param trip The TripEntity to insert.
     * @return The row ID of the newly inserted trip.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    /**
     * Updates an existing trip in the database.
     * @param trip The TripEntity with updated information.
     */
    @Update
    suspend fun updateTrip(trip: TripEntity)

    /**
     * Deletes a trip from the database.
     * @param trip The TripEntity to delete.
     */
    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    /**
     * Deletes all trips from the database.
     * Useful when logging out to clear data for the next user.
     */
    @Query("DELETE FROM trips")
    suspend fun clearAllTrips()
}
