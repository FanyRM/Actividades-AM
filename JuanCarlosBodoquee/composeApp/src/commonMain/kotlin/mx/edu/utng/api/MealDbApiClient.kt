package mx.edu.utng.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MealDbApiClient {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val baseUrl = "https://www.themealdb.com/api/json/v1/1"

    suspend fun searchMealsByName(name: String): MealResponse {
        println("Buscando: $name")
        val url = "$baseUrl/search.php?s=$name"
        println("Realizando petición a: $url")

        try {
            val response = httpClient.get(url)
            println("Código de respuesta: ${response.status}")

            val responseBody = response.body<MealResponse>()
            println("Comidas encontradas: ${responseBody.meals?.size ?: 0}")

            return responseBody
        } catch (e: Exception) {
            println("Error en la petición: ${e.message}")
            e.printStackTrace()
            throw e
        }

    }
    suspend fun getMealById(id: String): MealResponse {
        return httpClient.get("$baseUrl/lookup.php") {
            parameter("i", id)
        }.body()
    }

    suspend fun getRandomMeal(): MealResponse {
        return httpClient.get("$baseUrl/random.php").body()
    }

    fun close() {
        httpClient.close()
    }
}

