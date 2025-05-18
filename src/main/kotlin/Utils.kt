package project

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    fun formatDate(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.format(formatter)
    }
}
