package project

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Serializable
data class Transaction(
    val transactionId: String,
    val memberId: String,
    val bookId: String,
    val borrowDate: String,
    var returnDate: String? = null
) {
    fun isOverdue(): Boolean {
        val borrowDateParsed = LocalDate.parse(borrowDate)
        val daysBorrowed = ChronoUnit.DAYS.between(borrowDateParsed, LocalDate.now())
        return returnDate == null && daysBorrowed > 14 // Assuming 14 days is the borrowing limit
    }
}

enum class TransactionType {
    BORROW, RETURN
}
