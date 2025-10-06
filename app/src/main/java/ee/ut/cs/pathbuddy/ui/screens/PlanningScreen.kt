package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ee.ut.cs.pathbuddy.PathBuddyApplication
import ee.ut.cs.pathbuddy.navigation.Screen
import ee.ut.cs.pathbuddy.ui.components.BackButton
import ee.ut.cs.pathbuddy.ui.viewmodel.PlanningEvent
import ee.ut.cs.pathbuddy.ui.viewmodel.PlanningViewModel

/**
 * A screen for users to input details for a new trip.
 * It includes fields for destination, dates, interests, and budget.
 *
 * @param navController The navigation controller for navigating back or to the new trip page.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanningScreen(navController: NavController) {
    val container = (LocalContext.current.applicationContext as PathBuddyApplication).container
    val viewModel: PlanningViewModel = viewModel(factory = PlanningViewModel.provideFactory(container))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // This effect listens for one-time events from the ViewModel, like navigation or snackbar messages.
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                // When a trip is saved, navigate to its details page and clear the back stack up to Home.
                is PlanningEvent.TripSaved -> {
                    navController.navigate(Screen.TripPage.createRoute(event.tripId)) {
                        popUpTo(Screen.Home.route)
                    }
                }
                // Show a snackbar message for errors or other info.
                is PlanningEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan Your Trip") },
                navigationIcon = { BackButton(navController) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.destination,
                onValueChange = viewModel::onDestinationChanged,
                label = { Text("Destination") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.destinationError != null,
                supportingText = {
                    uiState.destinationError?.let { Text(it) }
                }
            )

            OutlinedTextField(
                value = uiState.startDate,
                onValueChange = viewModel::onStartDateChanged,
                label = { Text("Start Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.startDateError != null,
                supportingText = {
                    uiState.startDateError?.let { Text(it) }
                }
            )

            OutlinedTextField(
                value = uiState.endDate,
                onValueChange = viewModel::onEndDateChanged,
                label = { Text("End Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.endDateError != null,
                supportingText = {
                    uiState.endDateError?.let { Text(it) }
                }
            )

            OutlinedTextField(
                value = uiState.interests,
                onValueChange = viewModel::onInterestsChanged,
                label = { Text("Interests / Activities") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            OutlinedTextField(
                value = uiState.budget,
                onValueChange = viewModel::onBudgetChanged,
                label = { Text("Budget (optional)") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.budget != null,
                supportingText = {
                    uiState.budget?.let { Text(it) }
                }
            )

            Button(
                onClick = viewModel::submit,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isSaving) "Saving..." else "Generate")
            }
        }
    }
}