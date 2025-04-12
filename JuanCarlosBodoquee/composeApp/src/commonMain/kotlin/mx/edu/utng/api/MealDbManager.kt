package mx.edu.utng.api
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.edu.utng.api.MealRepository
import mx.edu.utng.api.MealDbApiClient


class MealDbManager {
    private val apiClient = MealDbApiClient()
    private val repository = MealRepository(apiClient)
    private val coroutineScope = MainScope()

    fun searchMeals(query: String, onSuccess: (meals: List<Meal>) -> Unit, onError: (Exception) -> Unit) {
        coroutineScope.launch {
            try {
                val meals = repository.searchMealsByName(query)
                onSuccess(meals)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun getMealDetails(id: String, onSuccess: (meal: Meal?) -> Unit, onError: (Exception) -> Unit) {
        coroutineScope.launch {
            try {
                val meal = repository.getMealById(id)
                onSuccess(meal)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun getRandomMeal(onSuccess: (meal: Meal?) -> Unit, onError: (Exception) -> Unit) {
        coroutineScope.launch {
            try {
                val meal = repository.getRandomMeal()
                onSuccess(meal)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun cleanup() {
        apiClient.close()
    }
}
