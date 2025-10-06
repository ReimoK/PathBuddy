package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.pathbuddy.PathBuddyApplication
import ee.ut.cs.pathbuddy.ui.components.BackButton
import ee.ut.cs.pathbuddy.ui.viewmodel.TripViewModel
import java.time.format.DateTimeFormatter

/**
 * A screen that displays the detailed information for a specific trip.
 *
 * @param navController The navigation controller for handling back navigation.
 * @param tripId The ID of the trip to be displayed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPageScreen(navController: androidx.navigation.NavController, tripId: Int) {
    val container = (LocalContext.current.applicationContext as PathBuddyApplication).container
    val viewModel: TripViewModel = viewModel(factory = TripViewModel.provideFactory(container, tripId))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Details") },
                navigationIcon = { BackButton(navController) }
            )
        }
    ) { padding ->
        // Show a loading indicator while the trip data is being fetched.
        if (uiState.isLoading) {
            Text(
                text = "Loading trip...",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
            return@Scaffold
        }

        // Handle the case where the trip is not found.
        val trip = uiState.trip ?: run {
            Text(
                text = "Trip not found.",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(trip.destination, style = MaterialTheme.typography.titleLarge)
                    Text("${trip.startDate} → ${trip.endDate}", style = MaterialTheme.typography.bodyMedium)
                    trip.budgetCategory?.takeIf { it.isNotBlank() }?.let {
                        Text("Budget: $it", style = MaterialTheme.typography.bodySmall)
                    }
                    if (trip.interests.isNotBlank()) {
                        Text("Focus: ${trip.interests}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Text("Trip Days", style = MaterialTheme.typography.titleMedium)

            // Display the itinerary timeline or a message if dates are invalid.
            if (uiState.dateRange.isEmpty()) {
                Text("Unable to generate itinerary timeline (check your dates).")
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val formatter = DateTimeFormatter.ofPattern("MMM d")
                    uiState.dateRange.forEachIndexed { index, date ->
                        Card {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("Day ${index + 1}", style = MaterialTheme.typography.labelLarge)
                                Text(formatter.format(date), style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "No activities yet.                                            Tap 'Map & Attractions' to explore.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            Button(onClick = { /* TODO: hook into Maps */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Map & Attractions")
            }

            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Real-time Info", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Weather, alerts, and navigation will appear here once integrated.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}