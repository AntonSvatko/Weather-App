package com.test.weather.data

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.weather.data.database.dao.CityDao
import com.test.weather.data.entity.City
import com.test.weather.network.api.WeatherService
import com.test.weather.network.utill.BaseApiResponse
import com.test.weather.ui.viewmodel.SearchedCitiesPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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


    fun getAllCities(): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = { dao.getCities() }
        ).flow
    }

    fun getSearchedCities(newText: String) =
        Pager(PagingConfig(20)) { SearchedCitiesPagingSource(newText, dao) }
            .flow
            .flowOn(Dispatchers.IO)
}
