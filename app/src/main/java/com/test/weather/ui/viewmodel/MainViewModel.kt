package com.test.weather.ui.viewmodel

import android.graphics.Bitmap
import com.test.weather.data.WeatherRepository
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : CoroutineViewModel() {
    val cities
        get() = repository.parseJSON()

    suspend fun getOddPhoto(): Flow<Bitmap> {
        return repository.getOddPhoto()
    }

    suspend fun getEvenPhoto(): Flow<Bitmap> {
        return repository.getEvenPhoto()
    }
}