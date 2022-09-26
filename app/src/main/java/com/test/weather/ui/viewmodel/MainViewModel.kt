package com.test.weather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.weather.data.WeatherRepository
import com.test.weather.data.entity.City
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : CoroutineViewModel() {
    val searchedCitiesFlow = MutableStateFlow<PagingData<City>>(PagingData.empty())

    //    val citiesList = repository.getCities()
    var list = listOf<City>()
    var i = 1

    private var lastSearch = ""

//    val randomUserPostFlow = Pager(PagingConfig(10)) { UserPostPagingSource(repository.dao) }
//        .flow
//        .cachedIn(viewModelScope)
//
//    init {
//        launchSafely {
//            randomUserPostFlow.collectLatest {
//                searchedCitiesFlow.emit(it)
//            }
//        }
//    }

    //    val getCity = repository.getScansFlow()
    fun getScansFlow(): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = { repository.dao.getCities() }
        ).flow.cachedIn(viewModelScope)
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

    val ld = MutableLiveData<PagingData<City>>()

    val getCities = debounce<String>(scope = viewModelScope) { newText ->
        launchSafely {
            if (lastSearch != newText) {
//                repository.getSearchedCities(newText).collect{
//                    searchedCitiesFlow.emit(it)
//                }

                Pager(PagingConfig(20)) { SearchedCitiesPagingSource(newText, repository.dao) }
                    .flow
                    .flowOn(Dispatchers.Default)
                    .cachedIn(viewModelScope).collect {
//                        searchedCitiesFlow.emit(it)
                        ld.postValue(it)
                    }

//                Pager(
//                    config = PagingConfig(
//                        pageSize = 10,
//                    ),
//                    pagingSourceFactory = { repository.dao.getSearchedCities(newText) }
//                ).flow
//                    .flowOn(Dispatchers.Default).cachedIn(viewModelScope).collectLatest {
//                    ld.value = it
//                }
            }
        }
        lastSearch = newText
    }
}