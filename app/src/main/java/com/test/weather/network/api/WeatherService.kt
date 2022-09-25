package com.test.weather.network.api

import com.test.weather.data.entity.weather.Weather
import com.test.weather.network.const.ApiConstants.API_KEY
import com.test.weather.network.const.ApiConstants.UNITS_SYSTEM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("/data/2.5/weather")
    suspend fun fetchWeather(
        @Query("lon") lon: Double = 0.0,
        @Query("lat") lat: Double = 0.0,
        @Query("appid") appId: String = API_KEY,
        @Query("units") units: String = UNITS_SYSTEM
    ): Response<Weather>
}