package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Welcome") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Welcome to PathBuddy",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "PathBuddy is your personal travel assistant. " +
                        "Plan your trips, navigate in real-time and capture memories easily!",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Use the menu on the Home page to start planning your first trip",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}