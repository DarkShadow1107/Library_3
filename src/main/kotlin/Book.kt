package project

import kotlinx.serialization.Serializable

@Serializable
enum class Genre {
    FICTION, NON_FICTION, SCIENCE, HISTORY, FANTASY, MYSTERY, BIOGRAPHY
}

@Serializable
class Book(
    val id: String,
    val title: String,
    val author: String,
    val genre: String,
    val year: Int,
    var isAvailable: Boolean = true
) {
    private val reviews = mutableListOf<Review>()

    fun borrow() {
        if (isAvailable) {
            isAvailable = false
            println("The book '$title' has been borrowed.")
        } else {
            println("The book '$title' is not available.")
        }
    }

    fun returnBook() {
        isAvailable = true
        println("The book '$title' has been returned.")
    }

    fun addReview(review: Review) {
        reviews.add(review)
        println("Review added for '$title': ${review.comment}")
    }

    fun listReviews() {
        if (reviews.isEmpty()) {
            println("No reviews for '$title'.")
        } else {
            println("Reviews for '$title':")
            reviews.forEach { println("- ${it.rating}/5: ${it.comment}") }
        }
    }

    fun getReviews(): List<Review> {
        return reviews
    }
}

@Serializable
data class Review(val rating: Int, val comment: String)
