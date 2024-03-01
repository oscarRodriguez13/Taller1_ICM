package com.example.taller1

data class WeatherResponse(
    val name: String, // Nombre de la ciudad
    val main: Main,
    val weather: List<Weather>
)