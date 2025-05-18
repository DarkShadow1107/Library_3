package project

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object FineManager {
    private const val DAILY_FINE = 1.0 // Fine per day in currency units

    fun calculateFine(transaction: Transaction): Double {
        val borrowDate = LocalDate.parse(transaction.borrowDate)
        val overdueDays = ChronoUnit.DAYS.between(borrowDate, LocalDate.now()) - 14
        return if (overdueDays > 0) overdueDays * DAILY_FINE else 0.0
    }

    fun listFines(library: Library) {
        val overdueTransactions = library.transactions.filter {
            it.returnDate == null && ChronoUnit.DAYS.between(
                LocalDate.parse(it.borrowDate),
                LocalDate.now()
            ) > 14
        }
        if (overdueTransactions.isEmpty()) {
            println("No fines to calculate.")
        } else {
            println("Fines for overdue books:")
            overdueTransactions.forEach {
                val fine = calculateFine(it)
                println("- Member ID: ${it.memberId} owes $fine for Book ID: '${it.bookId}'.")
            }
        }
    }
}
