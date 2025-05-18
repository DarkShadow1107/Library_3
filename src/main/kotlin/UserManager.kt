package project

object UserManager {
    private val users = mutableMapOf<String, String>() // Stores email-password pairs

    fun registerUser(email: String, password: String): Boolean {
        if (users.containsKey(email)) {
            return false // User already exists
        }
        users[email] = password
        return true
    }

    fun loginUser(email: String, password: String): Boolean {
        return users[email] == password
    }

    fun resetPassword(email: String, newPassword: String): Boolean {
        if (!users.containsKey(email)) {
            return false // User does not exist
        }
        users[email] = newPassword
        return true
    }
}