package project

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color

@Composable
@Preview
fun app() {
    LibraryTheme {
        var currentScreen by remember { mutableStateOf("MainMenu") }
        val library = remember {
            Library().apply {
                loadFromJson("library.json") // Load data from the JSON file during initialization
            }
        }

        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF121212)) // Set background color
        ) {
            when (currentScreen) {
                "MainMenu" -> MainMenu(onNavigate = { currentScreen = it }) // Updated to use mainMenu
                "AddBook" -> AddBookScreen(library, onBack = { currentScreen = "MainMenu" })
                "RegisterMember" -> RegisterMemberScreen(library, onBack = { currentScreen = "MainMenu" })
                "ListAvailableBooks" -> ListAvailableBooksScreen(library, onBack = { currentScreen = "MainMenu" })
                "BorrowBook" -> BorrowBookScreen(library, onBack = { currentScreen = "MainMenu" })
                "ReturnBook" -> ReturnBookScreen(library, onBack = { currentScreen = "MainMenu" })
                "SearchBooks" -> SearchBooksScreen(library, onBack = { currentScreen = "MainMenu" })
                "ListTransactions" -> ListTransactionsScreen(library, onBack = { currentScreen = "MainMenu" })
                "ListOverdueBooks" -> ListOverdueBooksScreen(library, onBack = { currentScreen = "MainMenu" })
                "GenerateReport" -> GenerateReportScreen(library, onBack = { currentScreen = "MainMenu" })
                "NotifyOverdueMembers" -> NotifyOverdueMembersScreen(library, onBack = { currentScreen = "MainMenu" })
                "ViewStatistics" -> ViewStatisticsScreen(library, onBack = { currentScreen = "MainMenu" })
                "AddBookReview" -> AddBookReviewScreen(library, onBack = { currentScreen = "MainMenu" })
                "ViewBookReviews" -> ViewBookReviewsScreen(library, onBack = { currentScreen = "MainMenu" })
                "ResetLibrary" -> ResetLibraryScreen(library, onBack = { currentScreen = "MainMenu" })
                "CalculateFines" -> CalculateFinesScreen(library, onBack = { currentScreen = "MainMenu" })
                "RecommendBooks" -> RecommendBooksScreen(library, onBack = { currentScreen = "MainMenu" })
                "ManageEvents" -> ManageEventsScreen(library, onBack = { currentScreen = "MainMenu" })
                "DownloadBook" -> DownloadBookScreen(onBack = { currentScreen = "MainMenu" })
            }
        }
    }
}

fun main() = application {
    val icon: Painter = painterResource("library.png") // Set the app icon
    Window(
        onCloseRequest = ::exitApplication,
        title = "Library", // Set the app window title
        icon = icon,
        state = androidx.compose.ui.window.WindowState(placement = androidx.compose.ui.window.WindowPlacement.Maximized) // Open in full window mode
    ) {
        app()
    }
}
