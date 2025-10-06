package ee.ut.cs.pathbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ee.ut.cs.pathbuddy.ui.screens.AboutUsScreen
import ee.ut.cs.pathbuddy.ui.screens.HomeScreen
import ee.ut.cs.pathbuddy.ui.screens.PlanningScreen
import ee.ut.cs.pathbuddy.ui.screens.ProfileScreen
import ee.ut.cs.pathbuddy.ui.screens.TripPageScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AboutUs : Screen("about_us")
    object Planning : Screen("planning")
    object Profile : Screen("profile")
    object TripPage : Screen("trip_page/{tripId}") {
        fun createRoute(tripId: Int) = "trip_page/$tripId"
    }
}

@Composable
fun PathBuddyNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.AboutUs.route) {
            AboutUsScreen(navController)
        }


        composable(Screen.Planning.route) {
            PlanningScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(
            route = Screen.TripPage.route,
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId") ?: 0
            TripPageScreen(navController, tripId)
        }

    }
}
