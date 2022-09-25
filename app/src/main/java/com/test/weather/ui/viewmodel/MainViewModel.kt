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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: WeatherRepository,
) : CoroutineViewModel() {
    val citiesList = repository.parseJSON()
    val searchedCitiesFlow = MutableStateFlow(citiesList.orEmpty())
    var lastSearch = ""

    private fun <T> debounce(
        waitMs: Long = 500L,
        scope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = scope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }

    val getCities = debounce<String>(scope = viewModelScope) { newText ->
        launchSafely {
//            if (lastSearch != newText) {
            searchedCitiesFlow.emit(citiesList?.filter {
                it.name.contains(newText, true)
            }.orEmpty())
//            }
        }
        lastSearch = newText
    }
}