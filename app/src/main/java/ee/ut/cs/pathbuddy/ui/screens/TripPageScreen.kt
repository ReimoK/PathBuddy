package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPageScreen(navController: NavController, tripId: Int) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Trip Details") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Trip ID: $tripId")
            Text("Trip Days:")
            // TODO: Days horizontal list
            Button(onClick = { /* TODO: Open Map */ }) {
                Text("Map & Attractions")
            }
            Text("Real-time Info (Weather, etc.)")
        }
    }
}
