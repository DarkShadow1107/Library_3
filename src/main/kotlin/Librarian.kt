package project

class Librarian(val name: String, val role: Role = Role.STAFF) {
    fun addBookToLibrary(library: Library, book: Book) {
        if (role.canAddBooks) {
            library.addBook(book)
            println("$name added '${book.title}' to the library.")
        } else {
            println("$name does not have permission to add books.")
        }
    }

    fun removeBookFromLibrary(library: Library, book: Book) {
        if (role.canRemoveBooks) {
            library.removeBook(book)
            println("$name removed '${book.title}' from the library.")
        } else {
            println("$name does not have permission to remove books.")
        }
    }

    fun registerMemberToLibrary(library: Library, member: Member) {
        if (role.canRegisterMembers) {
            library.registerMember(member)
            println("$name registered member '${member.name}'.")
        } else {
            println("$name does not have permission to register members.")
        }
    }
}

enum class Role(val canAddBooks: Boolean, val canRemoveBooks: Boolean, val canRegisterMembers: Boolean) {
    ADMIN(true, true, true),
    STAFF(true, false, true),
    ASSISTANT(false, false, false)
}
