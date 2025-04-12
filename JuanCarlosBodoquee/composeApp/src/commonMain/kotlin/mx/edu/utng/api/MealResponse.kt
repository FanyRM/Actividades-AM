package mx.edu.utng.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    val meals: List<Meal>? = null
)

@Serializable
data class Meal(
    @SerialName("idMeal") val id: String,
    @SerialName("strMeal") val name: String,
    @SerialName("strCategory") val category: String? = null,
    @SerialName("strArea") val area: String? = null,
    @SerialName("strInstructions") val instructions: String? = null,
    @SerialName("strMealThumb") val thumbnailUrl: String? = null,
    @SerialName("strTags") val tags: String? = null,
    @SerialName("strYoutube") val youtubeUrl: String? = null
)