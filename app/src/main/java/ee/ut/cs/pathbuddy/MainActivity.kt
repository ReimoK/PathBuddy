package ee.ut.cs.pathbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import ee.ut.cs.pathbuddy.navigation.PathBuddyNavHost
import ee.ut.cs.pathbuddy.ui.theme.PathBuddyTheme

/**
 * The main and only activity in the application.
 * It sets up the Compose content, including the theme and navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PathBuddyTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    // Set up the navigation graph for the application.
                    PathBuddyNavHost(navController = navController)
                }
            }
        }
    }
}