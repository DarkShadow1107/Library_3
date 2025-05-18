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
fun ManageEventsScreen(library: Library, onBack: () -> Unit) {
    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    val allFieldsFilled = eventName.isNotBlank() && eventDate.isNotBlank() && eventDescription.isNotBlank()

    // Helper for date input, auto-inserts '-' and restricts month/day
    fun formatEventDateInput(input: String): String {
        val digits = input.filter { it.isDigit() }
        val sb = StringBuilder()
        var i = 0
        // Year
        while (i < digits.length && i < 4) {
            sb.append(digits[i])
            i++
        }
        if (digits.length > 4) sb.append('-')
        // Month
        if (digits.length > 4) {
            if (i < digits.length) {
                val m1 = digits[i]
                if (m1 > '1') sb.append('0') else sb.append(m1)
                i++
            }
            if (i < digits.length) {
                val m2 = digits[i]
                val month = ("" + sb[sb.length - 1] + m2).toInt()
                if (month in 1..12) sb.append(m2)
                i++
            }
        }
        if (digits.length > 6) sb.append('-')
        // Day
        if (digits.length > 6) {
            if (i < digits.length) {
                val d1 = digits[i]
                if (d1 > '3') sb.append('0') else sb.append(d1)
                i++
            }
            if (i < digits.length) {
                val d2 = digits[i]
                val day = ("" + sb[sb.length - 1] + d2).toInt()
                if (day in 1..31) sb.append(d2)
                i++
            }
        }
        return sb.toString().take(10)
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
                        value = eventName,
                        onValueChange = { eventName = it },
                        label = { Text("Event Name", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = eventDate,
                        onValueChange = {
                            val formatted = formatEventDateInput(it)
                            eventDate = formatted
                        },
                        label = { Text("Event Date (YYYY-MM-DD)", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp),
                        placeholder = { Text("YYYY-MM-DD", color = Color(0xFFB0B0B0)) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = eventDescription,
                        onValueChange = { eventDescription = it },
                        label = { Text("Event Description", color = Color(0xFFE0E0E0)) },
                        textStyle = TextStyle(color = Color(0xFFE0E0E0), fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            EventManager.addEvent(Event(eventName, eventDate, eventDescription))
                            onBack()
                        },
                        enabled = allFieldsFilled
                    ) {
                        Text("Add Event", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        EventManager.listEvents()
                    }) {
                        Text("List Events", color = Color.Black)
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
