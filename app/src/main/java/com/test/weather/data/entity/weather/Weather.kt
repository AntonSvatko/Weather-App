package com.test.weather.data.entity.weather


data class Weather(
    val base: String,
    val cod: Int,
    val id: Int,
    val main: Main,
    val weather: List<WeatherX>,
    val wind: Wind,
    val name: String
)