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
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun RegisterMemberScreen(library: Library, onBack: () -> Unit) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var joined by remember { mutableStateOf("") }
    val allFieldsFilled = id.isNotBlank() && name.isNotBlank() && joined.isNotBlank()
    val memberIdPrefix = "M"
    var memberIdFocused by remember { mutableStateOf(false) }

    // Helper for date input
    fun formatJoinDateInput(input: String): String {
        // Only digits
        val digits = input.filter { it.isDigit() }
        val sb = StringBuilder()
        for (i in digits.indices) {
            val c = digits[i]
            // Year
            if (i < 4) {
                sb.append(c)
            }
            // After year, insert '-'
            if (i == 3 && digits.length > 4) sb.append('-')
            // Month
            if (i in 4..5) {
                // Only allow up to 2 digits for month
                if (i == 4) {
                    // First digit of month
                    if (c > '1') sb.append('0')
                    else sb.append(c)
                } else if (i == 5) {
                    val month = ("" + digits[4] + c).toInt()
                    if (month in 1..12) sb.append(c) else break
                }
            }
            // After month, insert '-'
            if (i == 5 && digits.length > 6) sb.append('-')
            // Day
            if (i in 6..7) {
                if (i == 6) {
                    // First digit of day
                    if (c > '3') sb.append('0')
                    else sb.append(c)
                } else if (i == 7) {
                    val day = ("" + digits[6] + c).toInt()
                    if (day in 1..31) sb.append(c) else break
                }
            }
        }
        return sb.toString()
            .let { it.take(10) } // Max length "YYYY-MM-DD"
    }

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
                        value = id,
                        onValueChange = { newValue -> id = newValue.filter { it.isDigit() } },
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
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = joined,
                        onValueChange = {
                            val formatted = formatJoinDateInput(it)
                            joined = formatted
                        },
                        label = { Text("Join Date (YYYY-MM-DD)", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp),
                        placeholder = { Text("YYYY-MM-DD", color = Color(0xFFB0B0B0)) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                val fullId = memberIdPrefix + id
                                library.registerMember(Member(fullId, name, joined))
                                onBack()
                            },
                            enabled = allFieldsFilled
                        ) {
                            Text("Register Member", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onBack) {
                            Text("Back", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
