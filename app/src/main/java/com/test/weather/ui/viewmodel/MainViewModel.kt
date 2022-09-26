package com.test.weather.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.test.weather.data.WeatherRepository
import com.test.weather.data.entity.City
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : CoroutineViewModel() {
    val searchedCitiesFlow = MutableStateFlow<List<City>>(listOf())
    val citiesList = repository.getCities()
    var list = listOf<City>()
    var i = 1

    var lastSearch = MutableStateFlow("")

    init {
        launchSafely {
            citiesList.collect {
                searchedCitiesFlow.emit(it)
            }
        }
    }


    fun loadCities(){
        lastSearch.map { it.trim() }
            .filter { it.isNotBlank() }
            .debounce(300L)
            .distinctUntilChanged()
            .flatMapLatest { repository.getSearchedCities(it) }
            .onEach { searchedCitiesFlow.emit(it) }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)

    }
}