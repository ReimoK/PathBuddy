package ee.ut.cs.pathbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ee.ut.cs.pathbuddy.data.AppContainer
import ee.ut.cs.pathbuddy.data.profile.ProfileData
import ee.ut.cs.pathbuddy.data.profile.ProfileRepository
import ee.ut.cs.pathbuddy.data.trip.Trip
import ee.ut.cs.pathbuddy.data.repository.TripRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Represents the UI state for the Home screen.
 *
 * @param profile The current user's profile data.
 * @param plannedTrips A list of upcoming or ongoing trips.
 * @param pastTrips A list of completed trips.
 */
data class HomeUiState(
    val profile: ProfileData = ProfileData(),
    val plannedTrips: List<Trip> = emptyList(),
    val pastTrips: List<Trip> = emptyList()
)

/**
 * ViewModel for the Home screen. It fetches and combines trip and profile data
 * to populate the UI.
 *
 * @param tripRepository The repository for accessing trip data.
 * @param profileRepository The repository for accessing profile data.
 */
class HomeViewModel(
    private val tripRepository: TripRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    /**
     * A StateFlow that holds the current UI state for the Home screen.
     * It combines the latest data from trips and profile flows.
     */
    val uiState: StateFlow<HomeUiState> = combine(
        tripRepository.getTrips(),
        profileRepository.profile
    ) { trips, profile ->
        val today = LocalDate.now()
        // Partition the list of trips into two lists: planned and past.
        // A trip is considered "planned" if its end date is not before yesterday.
        val (planned, past) = trips.sortedBy { it.startDate }
            .partition { trip ->
                parseDate(trip.endDate)?.isAfter(today.minusDays(1)) ?: true
            }

        HomeUiState(
            profile = profile,
            plannedTrips = planned,
            pastTrips = past
        )
    }.stateIn(
        scope = viewModelScope,
        // The flow starts when a subscriber appears and stops 5 seconds after the last subscriber disappears.
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    /**
     * Safely parses a date string in ISO_LOCAL_DATE format.
     * @param raw The date string to parse.
     * @return A LocalDate object, or null if parsing fails.
     */
    private fun parseDate(raw: String): LocalDate? =
        try {
            LocalDate.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (ex: DateTimeParseException) {
            null
        }

    /**
     * Companion object to provide a factory for creating the HomeViewModel.
     * This is used for dependency injection.
     */
    companion object {
        fun provideFactory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    tripRepository = container.tripRepository,
                    profileRepository = container.profileRepository
                )
            }
        }
    }
}