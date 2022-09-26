package com.test.weather.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.test.weather.data.WeatherRepository
import com.test.weather.data.entity.City
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: WeatherRepository,
) : CoroutineViewModel() {
    val searchedCitiesFlow = MutableStateFlow<List<City>>(listOf())
    var list = listOf<City>()
    var i = 1

    private var lastSearch = ""

    init {
        launchSafely {
            repository.getCities().collect {
                searchedCitiesFlow.emit(it)
            }
        }
    }

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
            if (lastSearch != newText) {
                repository.getSearchedCities(newText).collect{
                    searchedCitiesFlow.emit(it)
                }
            }
        }
        lastSearch = newText
    }
}