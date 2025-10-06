package ee.ut.cs.pathbuddy.data.trip

import ee.ut.cs.pathbuddy.data.local.TripEntity

/**
 * Converts a database TripEntity object to a domain-layer Trip object.
 * This is used to map data from the data layer to the business logic/UI layer.
 * @return The corresponding Trip object.
 */
fun TripEntity.toDomain(): Trip =
    Trip(
        id = id,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        interests = interests,
        budgetCategory = budgetCategory
    )

/**
 * Converts a domain-layer Trip object to a database TripEntity object.
 * This is used to map data for insertion or updating in the database.
 * @return The corresponding TripEntity object.
 */
fun Trip.toEntity(): TripEntity =
    TripEntity(
        id = id,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        interests = interests,
        budgetCategory = budgetCategory
    )