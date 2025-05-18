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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.onFocusEvent

@Composable
fun AddBookReviewScreen(library: Library, onBack: () -> Unit) {
    var bookId by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var ratingError by remember { mutableStateOf(false) }
    val allFieldsFilled = bookId.isNotBlank() && rating.isNotBlank() && comment.isNotBlank()
    val bookIdPrefix = "B"
    val bookExists = library.books.any { it.id == bookIdPrefix + bookId }
    var bookIdFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

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
                    TextField(
                        value = rating,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { it.isDigit() }
                            rating = filtered
                            ratingError = rating.isNotEmpty() && (rating.toIntOrNull() == null || rating.toInt() !in 1..5)
                        },
                        label = { Text("Rating (1-5)", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = if (ratingError) Color.Red else Color(0xFFE0E0E0), fontSize = 16.sp),
                        isError = ratingError
                    )
                    if (ratingError) {
                        Text("Rating must be an integer from 1 to 5", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Comment", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                val fullBookId = bookIdPrefix + bookId
                                val book = library.books.find { it.id == fullBookId }
                                if (book != null) {
                                    book.addReview(Review(rating.toInt(), comment))
                                    message = "Review added successfully!"
                                } else {
                                    message = "Book not found."
                                }
                            },
                            enabled = allFieldsFilled && !ratingError
                        ) {
                            Text("Add Review", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onBack) {
                            Text("Back", color = Color.Black)
                        }
                    }
                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(message, color = Color(0xFFE0E0E0))
                    }
                }
            }
        }
    }
}
