package com.test.weather.ui.viewmodel

import android.graphics.BitmapFactory
import android.util.Log
import com.test.weather.data.WeatherRepository
import com.test.weather.ui.base.viewmodel.CoroutineViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : CoroutineViewModel() {
    val cities
        get() = repository.parseJSON()

    private var photos = MutableStateFlow<List<String>>(listOf())

    init {
        launchSafely {
            getPhotoFromServer().collectLatest {
                it.forEach {
                    val body = it
                    val bytes = body.bytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    Log.d("test1", "sda " + bitmap.width)
                }
            }
        }
    }

    private fun getPhotoFromServer(): Flow<List<ResponseBody>> {
        return flow {
            emit(repository.getPhotos())
        }.flowOn(Dispatchers.IO)
    }
}