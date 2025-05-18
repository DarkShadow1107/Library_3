package project

object Statistics {
    fun calculateMostBorrowedBooks(library: Library) {
        val borrowCounts = library.transactions
            .groupingBy { it.bookId }
            .eachCount()

        if (borrowCounts.isEmpty()) {
            println("No books have been borrowed yet.")
        } else {
            println("Most Borrowed Books:")
            borrowCounts.entries
                .sortedByDescending { it.value }
                .take(5)
                .forEach { (bookId, count) ->
                    val book = library.books.find { it.id == bookId }
                    if (book != null) {
                        println("- ${book.title} by ${book.author}: $count times")
                    }
                }
        }
    }

    fun calculateActiveMembers(library: Library) {
        val activeMembers = library.transactions
            .groupingBy { it.memberId }
            .eachCount()

        if (activeMembers.isEmpty()) {
            println("No active members found.")
        } else {
            println("Active Members:")
            activeMembers.entries
                .sortedByDescending { it.value }
                .take(5)
                .forEach { (memberId, count) ->
                    val member = library.members.find { it.id == memberId }
                    if (member != null) {
                        println("- ${member.name}: $count transactions")
                    }
                }
        }
    }
}
