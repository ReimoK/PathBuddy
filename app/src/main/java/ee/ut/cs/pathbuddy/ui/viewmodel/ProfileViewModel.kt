package ee.ut.cs.pathbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ee.ut.cs.pathbuddy.data.AppContainer
import ee.ut.cs.pathbuddy.data.profile.ProfileData
import ee.ut.cs.pathbuddy.data.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the Profile screen.
 *
 * @param name The user's name.
 * @param homeBase The user's home base/city.
 * @param favoriteInterests The user's favorite travel interests.
 * @param isLoading Flag indicating if the initial profile is being loaded.
 * @param isSaving Flag indicating if the profile is currently being saved.
 * @param statusMessage A message to be shown to the user (e.g., in a snackbar).
 */
data class ProfileUiState(
    val name: String = "",
    val homeBase: String = "",
    val favoriteInterests: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val statusMessage: String? = null
)

/**
 * ViewModel for the Profile screen. It handles loading and saving the user's profile data.
 *
 * @param profileRepository The repository for accessing profile data.
 */
class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    /**
     * Initializes the ViewModel by loading the user's profile data from the repository.
     */
    init {
        viewModelScope.launch {
            // Collect the first emission from the profile flow to populate the initial state.
            val profile = profileRepository.profile.first()
            _uiState.value = ProfileUiState(
                name = profile.name,
                homeBase = profile.homeBase,
                favoriteInterests = profile.favoriteInterests,
                isLoading = false
            )
        }
    }

    /** Handles changes to the name text field. */
    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value, statusMessage = null)
    }

    /** Handles changes to the home base text field. */
    fun onHomeBaseChanged(value: String) {
        _uiState.value = _uiState.value.copy(homeBase = value, statusMessage = null)
    }

    /** Handles changes to the interests text field. */
    fun onInterestsChanged(value: String) {
        _uiState.value = _uiState.value.copy(favoriteInterests = value, statusMessage = null)
    }

    /**
     * Saves the current profile data to the repository.
     */
    fun saveProfile() {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.value = current.copy(isSaving = true)
            profileRepository.updateProfile(
                ProfileData(
                    name = current.name.trim(),
                    homeBase = current.homeBase.trim(),
                    favoriteInterests = current.favoriteInterests.trim()
                )
            )
            _uiState.value = _uiState.value.copy(
                isSaving = false,
                statusMessage = "Profile saved!"
            )
        }
    }
    /**
     * Call this function after the status message has been shown to the user.
     * This prevents the message from re-appearing on configuration changes.
     */
    fun onStatusMessageShown() {
        _uiState.value = _uiState.value.copy(statusMessage = null)
    }

    /**
     * Companion object to provide a factory for creating the ProfileViewModel.
     */
    companion object {
        fun provideFactory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProfileViewModel(container.profileRepository)
            }
        }
    }
}