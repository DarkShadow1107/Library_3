package project

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFFBB86FC), // Light purple
    primaryVariant = Color(0xFF3700B3), // Dark purple
    secondary = Color(0xFF03DAC5), // Teal
    secondaryVariant = Color(0xFF018786), // Dark teal
    background = Color(0xFF121212), // Dark gray for background
    surface = Color(0xFF1E1E1E), // Slightly lighter gray for surfaces
    error = Color(0xFFCF6679), // Red for errors
    onPrimary = Color.White, // Text color on primary
    onSecondary = Color.Black, // Text color on secondary
    onBackground = Color(0xFFE0E0E0), // White-ish text on background
    onSurface = Color(0xFFE0E0E0), // White-ish text on surfaces
    onError = Color.Black // Text color on error
)

@Composable
fun LibraryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        content = content
    )
}
