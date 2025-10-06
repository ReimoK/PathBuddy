package ee.ut.cs.pathbuddy.data.profile

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// A constant for the DataStore file name.
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
    private val dataStore: DataStore<Preferences>
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
     */
    val profile: Flow<ProfileData> = dataStore.data
        .catch { exception ->
            // If an IOException occurs (e.g., file corruption), emit empty preferences
            // to avoid crashing the app. Otherwise, re-throw the exception.
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            // Map the raw Preferences object to a structured ProfileData object.
            ProfileData(
                name = preferences[Keys.NAME] ?: "",
                homeBase = preferences[Keys.HOME_BASE] ?: "",
                favoriteInterests = preferences[Keys.FAVORITE_INTERESTS] ?: ""
            )
        }

    /**
     * Suspended function to update the user's profile data in the DataStore.
     * @param profileData The new profile data to save.
     */
    suspend fun updateProfile(profileData: ProfileData) {
        dataStore.edit { prefs ->
            prefs[Keys.NAME] = profileData.name
            prefs[Keys.HOME_BASE] = profileData.homeBase
            prefs[Keys.FAVORITE_INTERESTS] = profileData.favoriteInterests
        }
    }
}