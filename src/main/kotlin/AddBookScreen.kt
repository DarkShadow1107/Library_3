package project

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.Color

@Composable
fun AddBookScreen(library: Library, onBack: () -> Unit) {
    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var yearError by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    val bookIdPrefix = "B"
    var bookIdFocused by remember { mutableStateOf(false) }
    
    // Check if book ID exists in real-time
    val bookIdExists = id.isNotBlank() && library.books.any { it.id == bookIdPrefix + id }
    
    // Enable button only if all fields are valid and ID is not duplicate
    val allFieldsFilled = id.isNotBlank() && title.isNotBlank() && author.isNotBlank() && 
                         genre.isNotBlank() && year.length == 4 && !yearError && !bookIdExists

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
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = id,
                            onValueChange = { newValue -> 
                                id = newValue.filter { it.isDigit() }
                                // Clear any previous error message if ID changes
                                if (message.contains("ID")) {
                                    message = ""
                                }
                            },
                            label = { Text("Book ID", color = Color(0xFFE0E0E0)) },
                            textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp),
                            isError = bookIdExists,
                            visualTransformation = VisualTransformation { text ->
                                val prefix = bookIdPrefix
                                val transformed = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color(0xFF00BFFF))) { append(prefix) }
                                    append(text)
                                }
                                TransformedText(
                                    transformed,
                                    object : OffsetMapping {
                                        override fun originalToTransformed(offset: Int) = (offset + prefix.length).coerceIn(0, transformed.length)
                                        override fun transformedToOriginal(offset: Int) = (offset - prefix.length).coerceIn(0, text.length)
                                    }
                                )
                            },
                            modifier = Modifier.onFocusEvent { bookIdFocused = it.isFocused }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title", color = Color(0xFFE0E0E0)) },
                            textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = author,
                            onValueChange = { author = it },
                            label = { Text("Author", color = Color(0xFFE0E0E0)) },
                            textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = genre,
                            onValueChange = { genre = it },
                            label = { Text("Genre", color = Color(0xFFE0E0E0)) },
                            textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = year,
                        onValueChange = {
                            val filtered = it.filter { c -> c.isDigit() }.take(4)
                            year = filtered
                            yearError = year.length == 4 && year.toIntOrNull()?.let { y -> y > currentYear } == true
                        },
                        label = { Text("Year", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = if (yearError) Color.Red else Color(0xFFE0E0E0), fontSize = 16.sp),
                        isError = yearError
                    )
                    if (yearError) {
                        Text("It's impossible", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                val fullId = bookIdPrefix + id
                                if (library.books.any { it.id == fullId }) {
                                    message = "A book with this ID already exists."
                                } else {
                                    library.addBook(Book(fullId, title, author, genre, year.toInt()))
                                    onBack()
                                }
                            },
                            enabled = allFieldsFilled
                        ) {
                            Text("Add Book", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onBack) {
                            Text("Back", color = Color.Black)
                        }
                    }
                    
                    // Display error message below the buttons
                    if (message.isNotEmpty() || bookIdExists) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (bookIdExists) "A book with this ID already exists." else message, 
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}
