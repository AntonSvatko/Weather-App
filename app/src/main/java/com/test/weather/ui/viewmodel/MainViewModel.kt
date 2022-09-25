package com.test.weather.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.test.weather.data.WeatherRepository
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
            if (lastSearch != newText) {
                searchedCitiesFlow.emit(citiesList?.filter {
                    it.name.contains(newText, true)
                }.orEmpty())
            }
        }
        lastSearch = newText
    }
}