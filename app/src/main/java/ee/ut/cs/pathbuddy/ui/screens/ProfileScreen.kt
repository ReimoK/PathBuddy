package ee.ut.cs.pathbuddy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import ee.ut.cs.pathbuddy.PathBuddyApplication
import ee.ut.cs.pathbuddy.ui.components.BackButton
import ee.ut.cs.pathbuddy.ui.viewmodel.ProfileViewModel

/**
 * A screen for viewing and editing the user's profile information.
 * It features a display mode and an edit mode, toggled by the user.
 *
 * @param navController The navigation controller for handling back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // Standard ViewModel and state setup
    val container = (LocalContext.current.applicationContext as PathBuddyApplication).container
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.provideFactory(container))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Local state to manage whether the UI is in "display" or "edit" mode.
    var isEditMode by remember { mutableStateOf(false) }

    // Effect to show snackbar messages from the ViewModel (e.g., "Profile saved!")
    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            // Reset the message after showing it so it doesn't reappear on config change
            viewModel.onStatusMessageShown()
        }
    }

    val primaryBlue = MaterialTheme.colorScheme.primary

    Scaffold(
        containerColor = if (isEditMode) MaterialTheme.colorScheme.surface else primaryBlue,
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = if (isEditMode) MaterialTheme.colorScheme.onSurface else Color.White) },
                navigationIcon = { BackButton(navController, tint = if (isEditMode) MaterialTheme.colorScheme.onSurface else Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isEditMode) MaterialTheme.colorScheme.surface else primaryBlue
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        // Conditionally display either the viewing UI or the editing UI.
        if (isEditMode) {
            EditProfileContent(
                padding = padding,
                uiState = uiState,
                viewModel = viewModel,
                onSaveClicked = {
                    viewModel.saveProfile()
                    isEditMode = false // Switch back to display mode after saving
                }
            )
        } else {
            DisplayProfileContent(
                navController = navController,
                padding = padding,
                uiState = uiState,
                primaryBlue = primaryBlue,
                onEditClicked = { isEditMode = true } // Switch to edit mode
            )
        }
    }
}

/**
 * Composable for the profile's DISPLAY mode.
 * Shows the user's information in a visually appealing, read-only format.
 */
@Composable
private fun DisplayProfileContent(
    navController: NavController,
    padding: PaddingValues,
    uiState: ee.ut.cs.pathbuddy.ui.viewmodel.ProfileUiState,
    primaryBlue: Color,
    onEditClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Use a generic icon for the profile picture
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile picture",
            modifier = Modifier.size(120.dp),
            tint = Color.White
        )

        // Display the user's name from the database, or a placeholder
        Text(
            text = uiState.name.ifBlank { "Your Name" },
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        // Button to switch to edit mode
        Button(
            onClick = onEditClicked,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = primaryBlue
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Edit Profile",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Card to display other profile details from the database
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileDetailRow(label = "Home Base", value = uiState.homeBase.ifBlank { "Not set" })
                Divider()
                ProfileDetailRow(label = "Favorite Interests", value = uiState.favoriteInterests.ifBlank { "Not set" })
            }
        }
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Logout")
        }
    }
}

/**
 * A helper composable to display a row of information in the profile card.
 */
@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}


/**
 * Composable for the profile's EDIT mode.
 * Shows text fields to allow the user to modify their data.
 */
@Composable
private fun EditProfileContent(
    padding: PaddingValues,
    uiState: ee.ut.cs.pathbuddy.ui.viewmodel.ProfileUiState,
    viewModel: ProfileViewModel,
    onSaveClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiState.name,
            onValueChange = viewModel::onNameChanged,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        )

        OutlinedTextField(
            value = uiState.homeBase,
            onValueChange = viewModel::onHomeBaseChanged,
            label = { Text("Home Base / City") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        )

        OutlinedTextField(
            value = uiState.favoriteInterests,
            onValueChange = viewModel::onInterestsChanged,
            label = { Text("Favorite Travel Interests") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onSaveClicked,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isSaving) "Saving..." else "Save Changes")
        }
    }
}