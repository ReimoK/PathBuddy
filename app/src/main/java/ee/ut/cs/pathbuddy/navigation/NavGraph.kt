package ee.ut.cs.pathbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import ee.ut.cs.pathbuddy.ui.screens.AboutUsScreen
import ee.ut.cs.pathbuddy.ui.screens.HomeScreen
import ee.ut.cs.pathbuddy.ui.screens.PlanningScreen
import ee.ut.cs.pathbuddy.ui.screens.ProfileScreen
import ee.ut.cs.pathbuddy.ui.screens.TripPageScreen
import ee.ut.cs.pathbuddy.ui.screens.LoginScreen
import ee.ut.cs.pathbuddy.ui.screens.SignUpScreen
import ee.ut.cs.pathbuddy.ui.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
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

    // Kontrollime, kas kasutaja on sisse logitud
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

    // Kui on loginud → Home, kui mitte → Login
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // LOGIN
        composable(route = Screen.Login.route) {
            val viewModel: AuthViewModel = viewModel()
            LoginScreen(
                navController = navController,
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // SIGN UP
        composable(route = Screen.SignUp.route) {
            val viewModel: AuthViewModel = viewModel()
            SignUpScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // HOME
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        // ABOUT US
        composable(Screen.AboutUs.route) {
            AboutUsScreen(navController)
        }

        // PLANNING
        composable(Screen.Planning.route) {
            PlanningScreen(navController)
        }

        // PROFILE
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // TRIP PAGE
        composable(
            route = Screen.TripPage.route,
            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId") ?: 0
            TripPageScreen(navController, tripId)
        }
    }
}
