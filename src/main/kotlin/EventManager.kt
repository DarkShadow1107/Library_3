package project

data class Event(val name: String, val date: String, val description: String)

object EventManager {
    private val events = mutableListOf<Event>()

    fun addEvent(event: Event) {
        events.add(event)
        println("Event '${event.name}' added on ${event.date}.")
    }

    fun listEvents() {
        if (events.isEmpty()) {
            println("No upcoming events.")
        } else {
            println("Upcoming Events:")
            events.forEach {
                println("- ${it.name} on ${it.date}: ${it.description}")
            }
        }
    }
}
