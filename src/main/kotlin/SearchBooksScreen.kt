package project

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.io.File

@Composable
fun SearchBooksScreen(library: Library, onBack: () -> Unit) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(listOf<Book>()) }
    var previewText by remember { mutableStateOf("") }
    var showPreview by remember { mutableStateOf(false) }
    var selectedBookId by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Center the form in the window
    ) {
        WindowBackground("main", modifier = Modifier.fillMaxSize()) // Keep animated background

        if (isSearchVisible) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF10343F).copy(alpha = 0.95f))
                    .padding(16.dp)
                    .width(650.dp)
                    .height(455.dp),
                contentAlignment = Alignment.TopCenter // Ensures content is aligned to the top center
            ) {
                val verticalOffset by animateDpAsState(
                    targetValue = if (query.isNotEmpty()) 16.dp else (455.dp / 2 - 56.dp), // Move a little bit more up in transition
                    animationSpec = tween(durationMillis = 500)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .offset(y = verticalOffset)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            results = if (query.isNotEmpty()) {
                                library.books.filter {
                                    it.title.contains(query, ignoreCase = true) ||
                                            it.author.contains(query, ignoreCase = true)
                                }
                            } else {
                                emptyList()
                            }
                        },
                        label = { Text("Search Query", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = onBack) {
                        Text("Back", color = Color.Black)
                    }
                    if (query.isNotEmpty() && results.isEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp)) // Add space to move message a little lower
                        Text("Book not found.", color = Color(0xFFE0E0E0))
                    } else if (query.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxHeight()) {
                            items(results) { book ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${book.title} by ${book.author}", color = Color(0xFFE0E0E0))
                                    Button(onClick = {
                                        selectedBookId = book.id
                                        previewText = if (File("resources/previews/${book.id}.txt").exists()) {
                                            File("resources/previews/${book.id}.txt").readText()
                                        } else {
                                            "Preview feature is coming soon. Stay with us 😊😁."
                                        }
                                        isSearchVisible = false
                                    }) {
                                        Text("Preview", color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF10343F).copy(alpha = 0.95f))
                    .padding(16.dp)
                    .width(650.dp) // Match preview form size
                    .height(455.dp) // Match preview form size
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center, // Center content vertically
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(previewText, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
                    Button(onClick = {
                        isSearchVisible = true
                    }) {
                        Text("Back", color = Color.Black)
                    }
                }
            }
        }
    }
}
