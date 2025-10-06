package ee.ut.cs.pathbuddy.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

/**
 * A reusable composable for a back button in the app bar.
 * When clicked, it navigates to the previous screen on the back stack.
 *
 * @param navController The NavController used to perform the back navigation.
 * @param tint The color of the icon. Defaults to the current local content color.
 */
@Composable
fun BackButton(navController: NavController, tint: Color = LocalContentColor.current) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            tint = tint
        )
    }
}