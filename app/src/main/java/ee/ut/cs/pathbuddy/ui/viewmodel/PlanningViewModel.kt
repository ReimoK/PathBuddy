package ee.ut.cs.pathbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ee.ut.cs.pathbuddy.data.AppContainer
import ee.ut.cs.pathbuddy.data.trip.Trip
import ee.ut.cs.pathbuddy.data.trip.TripRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private const val DEFAULT_BUDGET_OPTION = "Moderate"
private val ISO_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
private val ACCEPTED_DATE_FORMATS = listOf(
    DateTimeFormatter.ISO_LOCAL_DATE, // YYYY-MM-DD
    DateTimeFormatter.ofPattern("MM/dd/yyyy")
)

/**
 * Represents the UI state for the Planning screen.
 *
 * @param destination The trip destination input by the user.
 * @param startDate The start date input by the user.
 * @param endDate The end date input by the user.
 * @param interests The interests input by the user.
 * @param budget The budget category selected by the user.
 * @param destinationError Validation error message for the destination field.
 * @param startDateError Validation error message for the start date field.
 * @param endDateError Validation error message for the end date field.
 * @param isSaving A flag indicating if the trip is currently being saved.
 */
data class PlanningUiState(
    val destination: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val interests: String = "",
    val budget: String = DEFAULT_BUDGET_OPTION,
    val destinationError: String? = null,
    val startDateError: String? = null,
    val endDateError: String? = null,
    val isSaving: Boolean = false
)

/**
 * Sealed class representing one-time events that can be sent from the ViewModel to the UI.
 * This is used for actions like navigation or showing a snackbar.
 */
sealed class PlanningEvent {
    data class TripSaved(val tripId: Int) : PlanningEvent()
    data class ShowMessage(val message: String) : PlanningEvent()
}

/**
 * ViewModel for the trip planning screen. It handles user input, validation,
 * and saving the new trip.
 *
 * @param tripRepository The repository for saving trip data.
 */
class PlanningViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlanningUiState())
    val uiState: StateFlow<PlanningUiState> = _uiState

    // A Channel is used to send one-time events to the UI.
    private val _events = Channel<PlanningEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /** Handles changes to the destination text field. */
    fun onDestinationChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            destination = value,
            destinationError = null // Clear error on new input
        )
    }

    /** Handles changes to the start date text field. */
    fun onStartDateChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            startDate = value,
            startDateError = null // Clear error on new input
        )
    }

    /** Handles changes to the end date text field. */
    fun onEndDateChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            endDate = value,
            endDateError = null // Clear error on new input
        )
    }

    /** Handles changes to the interests text field. */
    fun onInterestsChanged(value: String) {
        _uiState.value = _uiState.value.copy(interests = value)
    }

    /** Handles changes to the budget text field. */
    fun onBudgetChanged(value: String) {
        _uiState.value = _uiState.value.copy(budget = value)
    }

    /**
     * Validates the form inputs and, if valid, saves the new trip.
     */
    fun submit() {
        val current = _uiState.value

        val parsedStart = parseDate(current.startDate)
        val parsedEnd = parseDate(current.endDate)

        // --- Input Validation ---
        val destinationError =
            if (current.destination.isBlank()) "Destination cannot be empty" else null

        val startDateError = when {
            current.startDate.isBlank() -> "Start date is required"
            parsedStart == null -> "Use YYYY-MM-DD or MM/DD/YYYY"
            else -> null
        }

        val endDateError = when {
            current.endDate.isBlank() -> "End date is required"
            parsedEnd == null -> "Use YYYY-MM-DD or MM/DD/YYYY"
            parsedStart != null && parsedEnd.isBefore(parsedStart) -> "End date cannot be before start date"
            else -> null
        }

        // If any validation errors exist, update the UI state and stop.
        if (destinationError != null || startDateError != null || endDateError != null) {
            _uiState.value = current.copy(
                destinationError = destinationError,
                startDateError = startDateError,
                endDateError = endDateError
            )
            return
        }

        // Format dates to a consistent ISO standard before saving.
        val isoStart = parsedStart!!.format(ISO_FORMATTER)
        val isoEnd = parsedEnd!!.format(ISO_FORMATTER)
        val budgetValue = current.budget.takeIf { it.isNotBlank() }

        viewModelScope.launch {
            try {
                _uiState.value = current.copy(isSaving = true)
                val tripId = tripRepository.saveTrip(
                    Trip(
                        destination = current.destination.trim(),
                        startDate = isoStart,
                        endDate = isoEnd,
                        interests = current.interests.trim(),
                        budgetCategory = budgetValue
                    )
                )
                _uiState.value = PlanningUiState() // Reset form on success
                _events.send(PlanningEvent.TripSaved(tripId))
            } catch (ex: Exception) {
                _uiState.value = current.copy(isSaving = false)
                _events.send(PlanningEvent.ShowMessage("Unable to save trip: ${ex.message}"))
            }
        }
    }

    /**
     * Parses a date string by trying multiple accepted formats.
     * @param raw The date string from the user input.
     * @return A LocalDate object, or null if parsing fails for all formats.
     */
    private fun parseDate(raw: String): LocalDate? {
        ACCEPTED_DATE_FORMATS.forEach { formatter ->
            try {
                return LocalDate.parse(raw, formatter)
            } catch (_: DateTimeParseException) {
                // Ignore and try the next format.
            }
        }
        return null
    }

    /**
     * Companion object to provide a factory for creating the PlanningViewModel.
     */
    companion object {
        fun provideFactory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlanningViewModel(container.tripRepository)
            }
        }
    }
}