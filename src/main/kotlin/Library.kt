package project

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

@Serializable
class Library {
    internal val books = mutableListOf<Book>()
    internal val members = mutableListOf<Member>()
    internal val transactions = mutableListOf<Transaction>()

    fun loadFromJson(filePath: String) {
        val jsonData = DataManager.loadFromFile(filePath)
        if (jsonData != null) {
            val libraryData = Json.decodeFromString<LibraryData>(jsonData)
            books.clear()
            books.addAll(libraryData.books)
            members.clear()
            members.addAll(libraryData.members)
            transactions.clear()
            transactions.addAll(libraryData.transactions)
            println("Library data loaded successfully.")
        } else {
            println("Failed to load library data.")
        }
    }

    fun saveToJson(filePath: String) {
        val libraryData = LibraryData(books, members, transactions)
        val jsonData = Json.encodeToString(libraryData)
        DataManager.saveToFile(jsonData, filePath)
        println("Library data saved successfully.")
    }

    fun addBook(book: Book) {
            if (books.any { it.id == book.id }) {
                println("Error: Book with ID ${book.id} already exists.")
                return
            }
        books.add(book)
        println("Added '${book.title}' by ${book.author} to the library.")
        saveToJson("library.json")
    }

    fun registerMember(member: Member) {
            if (members.any { it.id == member.id }) {
                println("Error: Member with ID ${member.id} already exists.")
                return
            }
        members.add(member)
        println("Registered member: ${member.name}.")
        saveToJson("library.json")
    }

    fun listAvailableBooks() {
        val availableBooks = books.filter { it.isAvailable }
        if (availableBooks.isEmpty()) {
            println("No books are currently available.")
        } else {
            println("Available books:")
            availableBooks.forEach { println("- ${it.title} by ${it.author}") }
        }
    }

    fun searchBooks(query: String) {
        val results = books.filter { it.title.contains(query, ignoreCase = true) || it.author.contains(query, ignoreCase = true) }
        if (results.isEmpty()) {
            println("No books found for query: $query")
        } else {
            println("Search results:")
            results.forEach { println("- ${it.title} by ${it.author} (${it.genre})") }
        }
    }

    fun removeBook(book: Book) {
        if (books.remove(book)) {
            println("Removed '${book.title}' by ${book.author} from the library.")
            saveToJson("library.json")
        } else {
            println("The book '${book.title}' is not in the library.")
        }
    }

    fun recordTransaction(member: Member, book: Book, type: TransactionType) {
        val transaction = Transaction(
            transactionId = "T${transactions.size + 1}",
            memberId = member.id,
            bookId = book.id,
            borrowDate = LocalDate.now().toString(),
            returnDate = if (type == TransactionType.RETURN) LocalDate.now().toString() else null
        )
        transactions.add(transaction)
        println("Transaction recorded: ${type.name} - ${book.title} by ${member.name}.")
        saveToJson("library.json")
    }

    fun listTransactions() {
        if (transactions.isEmpty()) {
            println("No transactions recorded.")
        } else {
            println("Transaction history:")
            transactions.forEach { transaction ->
                val member = members.find { it.id == transaction.memberId }
                val book = books.find { it.id == transaction.bookId }
                if (member != null && book != null) {
                    println("- Transaction ID: ${transaction.transactionId}, Book: '${book.title}' by ${book.author}, Member: ${member.name}, Borrowed: ${transaction.borrowDate}, Returned: ${transaction.returnDate ?: "Not yet"}")
                }
            }
        }
    }

    fun listOverdueBooks() {
        val overdueTransactions = transactions.filter {
            it.returnDate == null && ChronoUnit.DAYS.between(
                LocalDate.parse(it.borrowDate),
                LocalDate.now()
            ) > 14
        }
        if (overdueTransactions.isEmpty()) {
            println("No overdue books.")
        } else {
            println("Overdue books:")
            overdueTransactions.forEach { transaction ->
                val member = members.find { it.id == transaction.memberId }
                val book = books.find { it.id == transaction.bookId }
                if (member != null && book != null) {
                    println("- '${book.title}' borrowed by ${member.name} on ${transaction.borrowDate}")
                }
            }
        }
    }

    fun generateReport() {
        println("\n--- Library Report ---")
        println("Total Books: ${books.size}")
        println("Total Members: ${members.size}")
        println("Total Transactions: ${transactions.size}")
        println("Books by Genre:")
        books.groupBy { it.genre }.forEach { (genre, books) ->
            println("- $genre: ${books.size}")
        }
    }

    fun saveData(filePath: String) {
        saveToJson(filePath)
    }

    companion object {
        fun loadData(filePath: String): Library? {
            val jsonData = DataManager.loadFromFile(filePath)
            return if (jsonData != null) {
                val libraryData = Json.decodeFromString<LibraryData>(jsonData)
                val library = Library()
                library.books.addAll(libraryData.books)
                library.members.addAll(libraryData.members)
                library.transactions.addAll(libraryData.transactions)
                library
            } else {
                null
            }
        }
    }

    fun resetLibrary() {
        books.clear()
        members.clear()
        transactions.clear()
        println("Library has been reset.")
        saveToJson("library.json")
    }
}

@Serializable
data class LibraryData(
    val books: List<Book>,
    val members: List<Member>,
    val transactions: List<Transaction>
)
