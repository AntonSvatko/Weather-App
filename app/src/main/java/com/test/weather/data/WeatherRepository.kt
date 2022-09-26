package com.test.weather.data

import com.test.weather.data.database.dao.CityDao
import com.test.weather.network.api.WeatherService
import com.test.weather.network.utill.BaseApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService,
    private val dao: CityDao
) : BaseApiResponse() {

    suspend fun getWeather(lon: Double, lat: Double) = flow {
        emit(safeApiCall { weatherService.fetchWeather(lon, lat) })
    }.flowOn(Dispatchers.IO)

    fun getCities() =
        dao.getCities()

    fun getSearchedCities(text: String) =
        dao.getSearchedCities(text)


}
