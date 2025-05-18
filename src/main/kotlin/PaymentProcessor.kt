package project

object PaymentProcessor {
    fun processPayment(userId: String, amount: Double): Boolean {
        // Placeholder for payment processing logic
        // In a real-world scenario, integrate with a payment gateway API
        println("Processing payment of $$amount for user $userId")
        return true // Assume payment is successful for now
    }
}