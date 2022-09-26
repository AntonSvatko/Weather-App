package com.test.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.weather.App
import com.test.weather.data.constants.Constants
import com.test.weather.data.database.dao.CityDao
import com.test.weather.data.entity.City
import com.test.weather.di.qualifier.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

//@TypeConverters(ImagesConverter::class)
@Database(entities = [City::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val cityDao: CityDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val climbingRouteDao = database.get().cityDao

            applicationScope.launch {
                prePopulateDatabase(climbingRouteDao)
            }
        }

        private suspend fun prePopulateDatabase(climbingRouteDao: CityDao) {
            val listType = object : TypeToken<List<City>>() {}.type
            val city = Gson().fromJson<List<City>>(readJSONFromAssets(), listType)

            climbingRouteDao.insert(city)
        }

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
    }
}