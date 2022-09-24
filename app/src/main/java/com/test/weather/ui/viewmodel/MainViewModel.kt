package com.test.weather.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.weather.data.WeatherRepository
import com.test.weather.data.entity.City
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : CoroutineViewModel() {
    var searchedCitiesFlow = MutableStateFlow<List<City>>(listOf())
    var photosLiveData = MutableLiveData<Pair<Flow<Bitmap>, Flow<Bitmap>>>()

    val citiesList = repository.parseJSON()
    var lastSearch = ""

    init {
        getPhotos()
    }

    private fun getPhotos() =
        launchSafely {
            val even = async { repository.getEvenPhoto() }
            val odd = async { repository.getOddPhoto() }
            photosLiveData.postValue(odd.await() to even.await())
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
                searchedCitiesFlow.emit(citiesList?.filter {
                    it.name.contains(newText, true)
                } ?: listOf())
            }
        }
        lastSearch = newText
    }
}