package ee.ut.cs.pathbuddy

import android.app.Application
import ee.ut.cs.pathbuddy.data.AppContainer
import ee.ut.cs.pathbuddy.data.DefaultAppContainer

/**
 * The main Application class for PathBuddy.
 * It is responsible for initializing the dependency container (AppContainer).
 */
class PathBuddyApplication : Application() {
    /**
     * The application-wide dependency container.
     * It is initialized once and can be accessed by any part of the app that has
     * access to the Application context.
     */
    lateinit var container: AppContainer
        private set

    /**
     * Called when the application is starting. This is where we initialize
     * the AppContainer.
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}