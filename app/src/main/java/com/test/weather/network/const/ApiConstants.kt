package com.test.weather.network.const

import com.test.weather.BuildConfig

object ApiConstants {
    const val BASE_API_LINK = "https://api.openweathermap.org/"

    const val API_KEY = BuildConfig.WEATHER_API_KEY
    const val UNITS_SYSTEM = "metric"
}