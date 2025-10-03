package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ee.ut.cs.pathbuddy.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.Planning.route) }) {
                Icon(Icons.Default.Add, contentDescription = "New Trip")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Welcome")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Planned Trips")
            // TODO: Trip list (LazyColumn)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Past Trips")
        }
    }
}
