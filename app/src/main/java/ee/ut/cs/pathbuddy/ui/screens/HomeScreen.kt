package ee.ut.cs.pathbuddy.ui.screens


import androidx.compose.foundation.clickable
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
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Planning.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Trip")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuButton(
                text = "Welcome",
                onClick = {
                    navController.navigate(Screen.Welcome.route)
                }
            )


            MenuButton(
                text = "Planned Trips",
                onClick = {
                    navController.navigate(Screen.Planning.route)
                }
            )

            MenuButton(
                text = "Past Trips",
                onClick = {
                    navController.navigate(Screen.TripPage.createRoute(1))
                }
            )
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
