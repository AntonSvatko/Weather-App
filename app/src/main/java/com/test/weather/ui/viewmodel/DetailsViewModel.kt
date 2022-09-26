package com.test.weather.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.weather.data.WeatherRepository
import com.test.weather.data.entity.Coord
import com.test.weather.data.entity.weather.Weather
import com.test.weather.network.utill.NetworkResult
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : CoroutineViewModel() {
    private val _weather = MutableLiveData<NetworkResult<Weather>>()
    val weatherResponse: LiveData<NetworkResult<Weather>> = _weather

    fun getWeather(coord: Coord) = viewModelScope.launch {
        repository.getWeather(coord.lon, coord.lat).collect {
            _weather.value = it
        }
    }
}