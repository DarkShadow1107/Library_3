package project

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import java.time.LocalDate
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
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun ReturnBookScreen(library: Library, onBack: () -> Unit) {
    val memberIdPrefix = "M"
    val bookIdPrefix = "B"
    var memberId by remember { mutableStateOf("") }
    var bookId by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var memberIdFocused by remember { mutableStateOf(false) }
    var bookIdFocused by remember { mutableStateOf(false) }
    val allFieldsFilled = memberId.isNotBlank() && bookId.isNotBlank()
    val memberExists = library.members.any { it.id == memberIdPrefix + memberId }
    val bookExists = library.books.any { it.id == bookIdPrefix + bookId }

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
                        value = memberId,
                        onValueChange = { newValue -> memberId = newValue.filter { it.isDigit() } },
                        label = { Text("Member ID", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp),
                        visualTransformation = VisualTransformation { text ->
                            val prefix = memberIdPrefix
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
                        modifier = Modifier.onFocusEvent { memberIdFocused = it.isFocused }
                    )
                    if (memberId.isNotBlank() && !memberExists) {
                        Text("Member not found", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                val fullMemberId = memberIdPrefix + memberId
                                val fullBookId = bookIdPrefix + bookId
                                val member = library.members.find { it.id == fullMemberId }
                                val book = library.books.find { it.id == fullBookId }
                                if (member != null && book != null) {
                                    // Update book availability
                                    book.isAvailable = true
                                    member.returnBook(book)
                                    
                                    // Update existing transaction
                                    val transaction = library.transactions.find { 
                                        it.memberId == fullMemberId && 
                                        it.bookId == fullBookId && 
                                        it.returnDate == null 
                                    }
                                    if (transaction != null) {
                                        transaction.returnDate = LocalDate.now().toString()
                                        message = "Book returned successfully!"
                                        library.saveToJson("library.json") // Save changes to JSON
                                    } else {
                                        message = "No borrowing record found for this book and member."
                                    }
                                } else {
                                    message = "Invalid Member ID or Book ID."
                                }
                            },
                            enabled = allFieldsFilled
                        ) {
                            Text("Return Book", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onBack) {
                            Text("Back", color = Color.Black)
                        }
                    }
                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(message, color = Color.White)
                    }
                }
            }
        }
    }
}
