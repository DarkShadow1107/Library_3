package project

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
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
fun RecommendBooksScreen(library: Library, onBack: () -> Unit) {
    var genre by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf(listOf<Book>()) }
    val allFieldsFilled = genre.isNotBlank() && author.isNotBlank()
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = genre,
                        onValueChange = { genre = it },
                        label = { Text("Genre", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            recommendations = library.books.filter {it.genre.equals(genre, ignoreCase = true) && it.author.equals(author, ignoreCase = true) }
                        },
                        enabled = allFieldsFilled
                    ) {
                        Text("Recommend Books", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    recommendations.forEach { book ->
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
