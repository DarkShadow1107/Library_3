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
fun GenerateReportScreen(library: Library, onBack: () -> Unit) {
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
                    Text("Library Report:", color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total Books: ${library.books.size}", color = Color(0xFFE0E0E0))
                    Text("Total Members: ${library.members.size}", color = Color(0xFFE0E0E0))
                    Text("Total Transactions: ${library.transactions.size}", color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Books by Genre:", color = Color(0xFFE0E0E0))
                    library.books.groupBy { it.genre }.forEach { (genre, books) ->
                        Text("- $genre: ${books.size}", color = Color(0xFFE0E0E0))
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
