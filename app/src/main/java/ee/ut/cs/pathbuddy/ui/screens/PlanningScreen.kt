package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ee.ut.cs.pathbuddy.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanningScreen(navController: NavController) {
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    val budgetOptions = listOf("Low", "Moderate", "High")
    var selectedBudget by remember { mutableStateOf(budgetOptions[1]) } // Default budget "Moderate"
    var budgetExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {},
                navigationIcon = {
                    TextButton(
                        onClick = { navController.popBackStack() },
                        contentPadding = PaddingValues(start = 16.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Dashboard")
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "Back to Dashboard",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 1. Header
            Text(
                "Plan Your Next\nAdventure",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Let our AI craft the perfect trip for you.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 2. Destination
            Text("Destination", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                placeholder = { Text("e.g., Paris, France") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 3. Start Date
            Text("Start Date", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                placeholder = { Text("mm/dd/yyyy") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = "Select Start Date",
                        modifier = Modifier.clickable { /* TODO: Open Date Picker */ }
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 4. End Date
            Text("End Date", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                placeholder = { Text("mm/dd/yyyy") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = "Select End Date",
                        modifier = Modifier.clickable { /* TODO: Open Date Picker */ }
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            //  5. Interests
            Text("Interests", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = interests,
                onValueChange = { interests = it },
                placeholder = { Text("e.g., History, Museums, Local Cuisine") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            //  6. Budget
            Text("Budget", style = MaterialTheme.typography.labelLarge)
            ExposedDropdownMenuBox(
                expanded = budgetExpanded,
                onExpandedChange = { budgetExpanded = !budgetExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedBudget,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            if (budgetExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Select Budget"
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = budgetExpanded,
                    onDismissRequest = { budgetExpanded = false }
                ) {
                    budgetOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedBudget = option
                                budgetExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // 7. Generate Button
            Button(
                onClick = {
                    // TODO: Collect data and send to generation service.
                    navController.navigate(Screen.Home.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = "Generate",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Generate Itinerary", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}
