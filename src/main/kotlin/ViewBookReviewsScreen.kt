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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.withStyle

@Composable
fun ViewBookReviewsScreen(library: Library, onBack: () -> Unit) {
    var bookId by remember { mutableStateOf("") }
    var reviews by remember { mutableStateOf(listOf<Review>()) }
    var message by remember { mutableStateOf("") }
    val allFieldsFilled = bookId.isNotBlank()
    val bookIdPrefix = "B"
    val bookExists = library.books.any { it.id == bookIdPrefix + bookId }
    var bookIdFocused by remember { mutableStateOf(false) }
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
                        value = bookId,
                        onValueChange = { newValue -> bookId = newValue.filter { it.isDigit() } },
                        label = { Text("Book ID", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp),
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
                    if (bookId.isNotBlank() && !bookExists) {
                        Text("Book not found", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val fullBookId = bookIdPrefix + bookId
                            val book = library.books.find { it.id == fullBookId }
                            if (book != null) {
                                reviews = book.getReviews()
                                message = ""
                            } else {
                                reviews = emptyList()
                                message = "Book not found"
                            }
                        },
                        enabled = allFieldsFilled
                    ) {
                        Text("View Reviews", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    reviews.forEach { review ->
                        Text("- ${review.rating}/5: ${review.comment}", color = Color(0xFFE0E0E0))
                    }
                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(message, color = Color.White)
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
