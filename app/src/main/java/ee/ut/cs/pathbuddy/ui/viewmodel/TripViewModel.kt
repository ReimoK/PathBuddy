package ee.ut.cs.pathbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ee.ut.cs.pathbuddy.data.AppContainer
import ee.ut.cs.pathbuddy.data.trip.Trip
import ee.ut.cs.pathbuddy.data.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Represents the UI state for the Trip details screen.
 *
 * @param trip The trip data being displayed, or null if not found.
 * @param isLoading A flag indicating if the trip data is currently being loaded.
 */
data class TripUiState(
    val trip: Trip? = null,
    val isLoading: Boolean = true
) {
    /**
     * A computed property that generates a list of LocalDates representing the
     * range of the trip, from start to end inclusive. Returns an empty list
     * if the dates are invalid or the trip is null.
     */
    val dateRange: List<LocalDate>
        get() {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val start = trip?.startDate?.let { parseDate(it, formatter) } ?: return emptyList()
            val end = trip.endDate?.let { parseDate(it, formatter) } ?: return emptyList()
            // Generate a sequence of dates starting from 'start' and incrementing by one day
            // until 'end' is reached.
            return generateSequence(start) { previous ->
                if (previous.isBefore(end)) previous.plusDays(1) else null
            }.toList()
        }
}

/**
 * Safely parses a date string using a specific formatter.
 * @param raw The date string to parse.
 * @param formatter The DateTimeFormatter to use.
 * @return A LocalDate object, or null if parsing fails.
 */
private fun parseDate(raw: String, formatter: DateTimeFormatter): LocalDate? =
    try {
        LocalDate.parse(raw, formatter)
    } catch (ex: DateTimeParseException) {
        null
    }

/**
 * ViewModel for the Trip details screen. It is responsible for fetching the
 * data for a single trip by its ID.
 *
 * @param tripRepository The repository for accessing trip data.
 * @param tripId The ID of the trip to be displayed.
 */
class TripViewModel(
    private val tripRepository: TripRepository,
    private val tripId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState

    /**
     * Initializes the ViewModel by starting a coroutine to collect the trip data
     * from the repository.
     */
    init {
        viewModelScope.launch {
            tripRepository.getTrip(tripId).collectLatest { trip ->
                _uiState.value = TripUiState(
                    trip = trip,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Companion object to provide a factory for creating the TripViewModel.
     * This allows passing the tripId to the ViewModel upon creation.
     */
    companion object {
        fun provideFactory(container: AppContainer, tripId: Int): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TripViewModel(
                        tripRepository = container.tripRepository,
                        tripId = tripId
                    )
                }
            }
    }
}