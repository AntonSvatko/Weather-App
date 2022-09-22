package com.test.weather.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.weather.App
import com.test.weather.data.entity.City
import com.test.weather.network.api.GifService
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val gifService: GifService,
) {
    private fun readJSONFromAssets(): String {
        var json = ""
        val inputStream = App.applicationContext?.assets?.open(Constants.FILE_NAME)
        if (inputStream != null) {
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
        }
        return json
    }

    suspend fun getPhotos() =
        listOf(gifService.fetchOddPhoto(), gifService.fetchEvenPhoto())


    fun parseJSON(): List<City>? {
        val listType = object : TypeToken<List<City?>?>() {}.type
        return Gson().fromJson(readJSONFromAssets(), listType)
    }
}