package mx.edu.utng.api
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    // Crear el repositorio
    val apiClient = remember { MealDbApiClient() }
    val repository = remember { MealRepository(apiClient) }

    // Estado para la UI
    var searchText by remember { mutableStateOf("") }
    var meals by remember { mutableStateOf<List<Meal>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Colores personalizados para la temática de cocina
    val primaryColor = Color(0xFFFF5722) // Color naranja cálido
    val secondaryColor = Color(0xFF4CAF50) // Color verde para acentos
    val backgroundColor = Color(0xFFFFF3E0) // Fondo crema suave
    val cardColor = Color.White

    // Tema personalizado
    MaterialTheme(
        colors = lightColors(
            primary = primaryColor,
            primaryVariant = primaryColor.copy(alpha = 0.8f),
            secondary = secondaryColor,
            background = backgroundColor,
            surface = cardColor
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Título de la aplicación
                Text(
                    text = "Recetas Deliciosas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo de búsqueda con estilo mejorado
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("¿Qué te gustaría cocinar hoy?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = primaryColor.copy(alpha = 0.5f)
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (searchText.isNotBlank()) {
                                    isLoading = true
                                    scope.launch {
                                        try {
                                            val result = repository.searchMealsByName(searchText)
                                            meals = result
                                        } catch (e: Exception) {
                                            println("Error de búsqueda: ${e.message}")
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = primaryColor
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Estado de carga
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = secondaryColor)
                    }
                } else if (meals.isEmpty() && searchText.isNotBlank()) {
                    // Mensaje cuando no hay resultados
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = cardColor,
                        elevation = 4.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No se encontraron resultados para: $searchText",
                                style = MaterialTheme.typography.body1,
                                color = Color.Gray
                            )
                            Text(
                                text = "Intenta con otro término de búsqueda",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                } else {
                    // Lista de resultados
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(meals) { meal ->
                            RecipeCard(meal)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(meal: Meal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Placeholder para imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                // Aquí es donde normalmente iría la imagen
                Text(
                    text = meal.name.first().toString(),
                    style = MaterialTheme.typography.h3,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF5722))
                        .wrapContentSize(Alignment.Center)
                )

                // Etiqueta de categoría
                meal.category?.let {
                    Surface(
                        color = Color(0xFFFF5722).copy(alpha = 0.8f),
                        shape = RoundedCornerShape(bottomEnd = 16.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = it,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Información de la receta
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                meal.area?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(8.dp),
                            shape = CircleShape,
                            color = Color(0xFF4CAF50)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cocina $it",
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                meal.tags?.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.caption,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar instrucciones (primeros caracteres)
                meal.instructions?.let {
                    val shortInstructions = if (it.length > 100) it.substring(0, 100) + "..." else it
                    Text(
                        text = shortInstructions,
                        style = MaterialTheme.typography.body2,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}