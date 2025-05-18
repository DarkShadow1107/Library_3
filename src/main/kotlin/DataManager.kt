package project

import java.io.File
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

object DataManager {
    private val json = Json { prettyPrint = true }

    fun <T> saveToFile(data: T, filePath: String, serializer: KSerializer<T>) {
        val file = File("src/main/data/$filePath")
        file.writeText(json.encodeToString(serializer, data))
    }

    fun <T> loadFromFile(filePath: String, serializer: KSerializer<T>): T? {
        val file = File("src/main/data/$filePath")
        return if (file.exists()) {
            json.decodeFromString(serializer, file.readText())
        } else {
            null
        }
    }

    fun saveToFile(data: String, filePath: String) {
        val file = File("src/main/data/$filePath")
        file.writeText(data)
    }

    fun loadFromFile(filePath: String): String? {
        val file = File("src/main/data/$filePath")
        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }
}
