package ee.ut.cs.pathbuddy.data.profile

/**
 * Represents the user's profile data.
 * This data class is used to hold profile information within the application.
 *
 * @param name The user's name.
 * @param homeBase The user's home city or location.
 * @param favoriteInterests A string describing the user's favorite travel interests.
 */
data class ProfileData(
    val name: String = "",
    val homeBase: String = "",
    val favoriteInterests: String = ""
)