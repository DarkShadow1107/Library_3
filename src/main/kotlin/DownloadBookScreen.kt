package project

import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DownloadBookScreen(onBack: () -> Unit) {
    val searchQuery = remember { mutableStateOf("") }
    val allBooks = listOf(
        "Book1" to "Available for download.",
        "Book2" to "Available for download.",
        "Book3" to "Available for download."
    )
    val filteredBooks = remember(searchQuery.value) {
        allBooks.filter { it.first.contains(searchQuery.value, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Download Book") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedLibraryBackground(modifier = Modifier.fillMaxSize())

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                TextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = { Text("Search Books") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredBooks) { (bookId, status) ->
                        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Book ID: $bookId", style = MaterialTheme.typography.h6)
                                Text(status, style = MaterialTheme.typography.body1, modifier = Modifier.padding(bottom = 8.dp))
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Button(onClick = { println("Previewing $bookId") }) {
                                        Text("Preview")
                                    }
                                    Button(onClick = { println("Downloading $bookId") }) {
                                        Text("Download")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}