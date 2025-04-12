package mx.edu.utng.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealVieewModel(private val repository: MealRepository) : ViewModel() {
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun searchMeals(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = repository.searchMealsByName(query)
                _meals.value = result
                if (result.isEmpty()) {
                    _errorMessage.value = "No se encontraron recetas para \"$query\""
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener recetas: "
            } finally {
                _isLoading.value = false
            }
        }
    }
}
