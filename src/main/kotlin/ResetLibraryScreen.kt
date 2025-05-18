package project

import androidx.compose.foundation.layout.*
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

@Composable
fun ResetLibraryScreen(library: Library, onBack: () -> Unit) {
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
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Are you sure you want to reset the library?", color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            library.resetLibrary()
                            onBack()
                        }) {
                            Text("Yes, Reset", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onBack) {
                            Text("Cancel", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
