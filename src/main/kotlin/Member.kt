package project

import kotlinx.serialization.Serializable

@Serializable
class Member(
    val id: String,
    val name: String,
    val joined: String,
    private val maxBooksAllowed: Int = 3
) {
    private val borrowedBooks = mutableListOf<Book>()

    fun borrowBook(book: Book) {
        if (borrowedBooks.size >= maxBooksAllowed) {
            println("$name cannot borrow more than $maxBooksAllowed books.")
            return
        }
        if (book.isAvailable) {
            book.borrow()
            borrowedBooks.add(book)
        } else {
            println("$name cannot borrow '${book.title}' as it is not available.")
        }
    }

    fun returnBook(book: Book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook()
            borrowedBooks.remove(book)
        } else {
            println("$name has not borrowed '${book.title}'.")
        }
    }

    fun listBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            println("$name has not borrowed any books.")
        } else {
            println("$name has borrowed the following books:")
            borrowedBooks.forEach { println("- ${it.title} by ${it.author}") }
        }
    }
}
