package ee.ut.cs.pathbuddy.data.profile

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// A constant for the base DataStore file name.
private const val PROFILE_DATASTORE = "profile_preferences"

/**
 * Extension property on Context to provide a singleton instance of Preferences DataStore.
 */
val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(
    name = PROFILE_DATASTORE
)

/**
 * Repository for managing user profile data using Jetpack DataStore.
 * This class abstracts the data source for the profile information.
 *
 * @param dataStore The DataStore instance used for persisting profile data.
 */
class ProfileRepository(
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // 🔹 Firebase kasutaja identifikaator
) {

    /**
     * Private object to hold the keys for the preferences data store.
     * This prevents stringly-typed errors and centralizes key management.
     */
    private object Keys {
        val NAME = stringPreferencesKey("name")
        val HOME_BASE = stringPreferencesKey("home_base")
        val FAVORITE_INTERESTS = stringPreferencesKey("favorite_interests")
    }

    /**
     * A flow that emits the user's profile data whenever it changes.
     * Each logged-in user has their own isolated data, keyed by UID.
     */
    val profile: Flow<ProfileData> = dataStore.data
        .catch { exception ->
            // If an IOException occurs (e.g., file corruption), emit empty preferences
            // to avoid crashing the app. Otherwise, re-throw the exception.
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val uid = auth.currentUser?.uid ?: "guest"
            // Each field is stored under a UID-specific key.
            ProfileData(
                name = preferences[stringPreferencesKey("${uid}_name")] ?: "",
                homeBase = preferences[stringPreferencesKey("${uid}_home_base")] ?: "",
                favoriteInterests = preferences[stringPreferencesKey("${uid}_favorite_interests")] ?: ""
            )
        }

    /**
     * Suspended function to update the user's profile data in the DataStore.
     * Each user's data is stored under their own UID to keep profiles separate.
     * @param profileData The new profile data to save.
     */
    suspend fun updateProfile(profileData: ProfileData) {
        val uid = auth.currentUser?.uid ?: return
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("${uid}_name")] = profileData.name
            prefs[stringPreferencesKey("${uid}_home_base")] = profileData.homeBase
            prefs[stringPreferencesKey("${uid}_favorite_interests")] = profileData.favoriteInterests
        }
    }

    /**
     * Clears the current user's stored profile data.
     * Useful when logging out to prevent another user from seeing previous info.
     */
    suspend fun clearProfile() {
        val uid = auth.currentUser?.uid ?: return
        dataStore.edit { prefs ->
            prefs.remove(stringPreferencesKey("${uid}_name"))
            prefs.remove(stringPreferencesKey("${uid}_home_base"))
            prefs.remove(stringPreferencesKey("${uid}_favorite_interests"))
        }
    }
}
