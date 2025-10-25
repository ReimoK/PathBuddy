package ee.ut.cs.pathbuddy.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import ee.ut.cs.pathbuddy.data.repository.AuthRepository
import ee.ut.cs.pathbuddy.data.repository.TripRepository
import ee.ut.cs.pathbuddy.data.profile.ProfileRepository // 🔹 lisatud profiili jaoks
import kotlinx.coroutines.launch

/**
 * ViewModel, mis haldab Firebase Auth seisundit ja UI olekut.
 */
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: FirebaseUser?) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository(),
    private val tripRepository: TripRepository? = null, // 🔹 kustutab tripid logouti ajal
    private val profileRepository: ProfileRepository? = null // 🔹 lisatud, et puhastada profiil logouti ajal
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            val result = repo.login(email, password)
            result.onSuccess {
                uiState = AuthUiState.Success(it)
                onSuccess()
            }.onFailure {
                uiState = AuthUiState.Error(it.message ?: "Login failed")
                onError(it.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            val result = repo.signUp(email, password)
            result.onSuccess {
                uiState = AuthUiState.Success(it)
                onSuccess()
            }.onFailure {
                uiState = AuthUiState.Error(it.message ?: "Sign-up failed")
                onError(it.message ?: "Sign-up failed")
            }
        }
    }

    /**
     * Logib kasutaja välja ja puhastab lokaalsed andmed (tripid ja profiil),
     * et järgmine kasutaja ei näeks eelmise andmeid.
     */
    fun logout() {
        viewModelScope.launch {
            tripRepository?.clearAllTrips() // 🔹 kustutab lokaalsed tripid
            profileRepository?.clearProfile() // 🔹 kustutab profiili DataStore’ist
            repo.logout()
            uiState = AuthUiState.Idle
        }
    }
}
