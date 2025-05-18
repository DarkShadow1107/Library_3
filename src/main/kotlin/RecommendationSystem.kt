package project

object RecommendationSystem {
    fun recommendBooks(library: Library, genre: String) {
        val recommendedBooks = library.books.filter { it.genre == genre && it.isAvailable }
        if (recommendedBooks.isEmpty()) {
            println("No available books found in the genre: $genre.")
        } else {
            println("Recommended books in the genre $genre:")
            recommendedBooks.forEach { println("- ${it.title} by ${it.author}") }
        }
    }
}
