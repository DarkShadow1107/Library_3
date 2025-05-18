package project

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object NotificationManager {
    fun notifyOverdueBooks(library: Library) {
        val overdueTransactions = library.transactions.filter {
            it.returnDate == null && ChronoUnit.DAYS.between(
                LocalDate.parse(it.borrowDate),
                LocalDate.now()
            ) > 14
        }

        if (overdueTransactions.isEmpty()) {
            println("No overdue notifications to send.")
        } else {
            println("Sending overdue notifications:")
            overdueTransactions.forEach { transaction ->
                val member = library.members.find { it.id == transaction.memberId }
                val book = library.books.find { it.id == transaction.bookId }
                if (member != null && book != null) {
                    println("Notification sent to ${member.name} for overdue book '${book.title}'.")
                }
            }
        }
    }
}
