package mx.edu.utng.api

import mx.edu.utng.api.MealDbApiClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MealRepository(private val apiClient: MealDbApiClient) {
    suspend fun searchMealsByName(name: String): List<Meal> = withContext(Dispatchers.Default) {
        val response = apiClient.searchMealsByName(name)
        return@withContext response.meals ?: emptyList()
    }

    suspend fun getMealById(id: String): Meal? = withContext(Dispatchers.Default) {
        val response = apiClient.getMealById(id)
        return@withContext response.meals?.firstOrNull()
    }

    suspend fun getRandomMeal(): Meal? = withContext(Dispatchers.Default) {
        val response = apiClient.getRandomMeal()
        return@withContext response.meals?.firstOrNull()
    }

}