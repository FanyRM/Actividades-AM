package mx.edu.utng.api

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform