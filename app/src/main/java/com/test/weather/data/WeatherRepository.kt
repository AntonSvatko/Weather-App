package com.test.weather.data

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.weather.data.database.dao.CityDao
import com.test.weather.data.entity.City
import com.test.weather.network.api.WeatherService
import com.test.weather.network.utill.BaseApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService,
    val dao: CityDao
) : BaseApiResponse() {

    suspend fun getWeather(lon: Double, lat: Double) = flow {
        emit(safeApiCall { weatherService.fetchWeather(lon, lat) })
    }.flowOn(Dispatchers.IO)


//    fun getCities() =
//        dao.getCities()

//    fun getSearchedCities(text: String) =
//        dao.getSearchedCities(text)
}
