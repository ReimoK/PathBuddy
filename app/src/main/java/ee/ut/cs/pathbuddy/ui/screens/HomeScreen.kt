package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.pathbuddy.PathBuddyApplication
import ee.ut.cs.pathbuddy.data.trip.Trip
import ee.ut.cs.pathbuddy.navigation.Screen
import ee.ut.cs.pathbuddy.ui.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * The main screen of the application, displaying a welcome message,
 * a button to plan a new trip, and a list of saved trips.
 *
 * @param navController The navigation controller for navigating to other screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: androidx.navigation.NavController) {
    // Obtain the app container and view model instance.
    val container = (LocalContext.current.applicationContext as PathBuddyApplication).container
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.provideFactory(container))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val primaryBlue = MaterialTheme.colorScheme.primary
    // Combine planned and past trips and sort them for a unified display.
    val savedTrips = (uiState.plannedTrips + uiState.pastTrips).sortedBy { it.startDate }

    Scaffold(
        containerColor = primaryBlue
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeHeader(
                name = uiState.profile.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Profile.route) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PlanNewAdventureButton(
                onClick = { navController.navigate(Screen.Planning.route) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.AboutUs.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About Us",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "About Us",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }



            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Your Saved Trips",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display a placeholder if there are no saved trips, otherwise show the list.
            if (savedTrips.isEmpty()) {
                EmptyTripsPlaceholder(modifier = Modifier.fillMaxWidth())
            } else {
                SavedTripsList(
                    trips = savedTrips,
                    onTripSelected = { tripId ->
                        navController.navigate(Screen.TripPage.createRoute(tripId))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * A composable that displays a list of trips in a LazyColumn.
 *
 * @param trips The list of trips to display.
 * @param onTripSelected A callback invoked when a trip is selected.
 * @param modifier The modifier to be applied to the LazyColumn.
 */
@Composable
private fun SavedTripsList(
    trips: List<Trip>,
    onTripSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = trips, key = { it.id }) { trip ->
            TripCard(
                trip = trip,
                onClick = { onTripSelected(trip.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * A card composable to display a summary of a single trip.
 *
 * @param trip The trip data to display.
 * @param onClick The callback to be invoked when the card is clicked.
 * @param modifier The modifier to be applied to the card.
 */
@Composable
private fun TripCard(trip: Trip, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.primaryDestination(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trip.secondaryLine(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icons.Default.ChevronRight.also { icon ->
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = "View Trip Details",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * A header card that welcomes the user.
 *
 * @param name The name of the user to display.
 * @param modifier The modifier to be applied to the card.
 */
@Composable
private fun WelcomeHeader(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "User Profile",
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                val greetingName = if (name.isBlank()) "Explorer" else name
                Text(
                    "Welcome, $greetingName!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Your travel journeys await.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * A large button for initiating the trip planning flow.
 *
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 */
@Composable
private fun PlanNewAdventureButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        androidx.compose.material3.Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "New Trip",
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Plan a New Adventure",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

/**
 * A placeholder card displayed when the user has no saved trips.
 *
 * @param modifier The modifier to be applied to the card.
 */
@Composable
private fun EmptyTripsPlaceholder(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "You have no saved trips.",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Time to plan your next getaway!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/* Helper functions for formatting Trip data for display */

private val displayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

/** Extracts the primary destination (e.g., city) from the full destination string. */
private fun Trip.primaryDestination(): String =
    destination.substringBefore(",").trim().ifBlank { destination.trim() }

/** Extracts the country from the destination string, if available. */
private fun Trip.countryOrNull(): String? =
    destination.substringAfter(",", "").trim().takeIf { it.isNotBlank() }

/** Formats the start and end dates into a displayable string range. */
private fun Trip.formattedDates(): String? =
    formatDate(startDate)?.let { start ->
        formatDate(endDate)?.let { end -> "$start - $end" }
    }

/** Creates a secondary line of text for the TripCard, combining country, dates, and budget. */
private fun Trip.secondaryLine(): String {
    val parts = mutableListOf<String>()
    countryOrNull()?.let(parts::add)
    formattedDates()?.let(parts::add)
    budgetCategory?.takeIf { it.isNotBlank() }?.let { parts.add("Budget: $it") }
    return parts.joinToString(" • ")
}

/** Safely parses and formats a single date string. */
private fun formatDate(raw: String): String? =
    try {
        LocalDate.parse(raw).format(displayFormatter)
    } catch (_: DateTimeParseException) {
        null
    }