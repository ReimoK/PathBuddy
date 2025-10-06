package ee.ut.cs.pathbuddy.data.trip

/**
 * Represents a trip in the domain layer of the application.
 * This data class is used throughout the UI and business logic.
 *
 * @param id The unique identifier for the trip.
 * @param destination The primary destination of the trip.
 * @param startDate The start date of the trip (YYYY-MM-DD).
 * @param endDate The end date of the trip (YYYY-MM-DD).
 * @param interests A string of user interests for the trip.
 * @param budgetCategory An optional budget category.
 */
data class Trip(
    val id: Int = 0,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val interests: String,
    val budgetCategory: String? = null
)