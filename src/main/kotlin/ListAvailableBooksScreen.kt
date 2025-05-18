package project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun ListAvailableBooksScreen(library: Library, onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        WindowBackground("main", modifier = Modifier.fillMaxSize())
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF10343F).copy(alpha = 0.95f))
                    .padding(32.dp)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Available Books:", color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(8.dp))
                    library.books.filter { it.isAvailable }.forEach { book ->
                        Text("- ${book.title} by ${book.author}", color = Color(0xFFE0E0E0))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack) {
                        Text("Back", color = Color.Black)
                    }
                }
            }
        }
    }
}
