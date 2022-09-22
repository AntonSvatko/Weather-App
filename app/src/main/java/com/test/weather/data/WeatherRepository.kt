package com.test.weather.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.weather.App
import com.test.weather.data.entity.City
import com.test.weather.network.api.GifService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val gifService: GifService,
) {
    suspend fun getOddPhoto(): Flow<Bitmap> {
        val oddPhoto =
            App.applicationContext?.filesDir?.absolutePath + File.separator + "Temp1.png"

        return if (!File(oddPhoto).exists())
            gifService.fetchOddPhoto().savePhotoToStorage(oddPhoto)
        else {
            val filePath = File(oddPhoto).absolutePath
            flow {
                emit(BitmapFactory.decodeFile(filePath))
            }
        }
    }

    suspend fun getEvenPhoto(): Flow<Bitmap> {
        val evenPhoto =
            App.applicationContext?.applicationInfo?.dataDir + File.separator + "Temp3.png"
        return if (!File(evenPhoto).exists())
            gifService.fetchEvenPhoto().savePhotoToStorage(evenPhoto)
        else {
            val filePath = File(evenPhoto).absolutePath
            flow {
                emit(BitmapFactory.decodeFile(filePath))
            }
        }
    }

    private fun ResponseBody.savePhotoToStorage(fileName: String) = flow<Bitmap> {
        kotlin.runCatching {
            val bytes = this@savePhotoToStorage.bytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            emit(bitmap)

            FileOutputStream(fileName).use { out ->
                bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    out
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

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

    fun parseJSON(): List<City>? {
        val listType = object : TypeToken<List<City?>?>() {}.type
        return Gson().fromJson(readJSONFromAssets(), listType)
    }
}
