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
fun NotifyOverdueMembersScreen(library: Library, onBack: () -> Unit) {
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
                    Text("Overdue Notifications:", color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(8.dp))
                    val overdueTransactions = library.transactions.filter {
                        it.returnDate == null && it.isOverdue()
                    }
                    if (overdueTransactions.isEmpty()) {
                        Text("No overdue notifications to send.", color = Color(0xFFE0E0E0))
                    } else {
                        overdueTransactions.forEach { transaction ->
                            val member = library.members.find { it.id == transaction.memberId }
                            val book = library.books.find { it.id == transaction.bookId }
                            if (member != null && book != null) {
                                Text(
                                    "- Notify ${member.name} for '${book.title}'",
                                    color = Color(0xFFE0E0E0)
                                )
                            }
                        }
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
