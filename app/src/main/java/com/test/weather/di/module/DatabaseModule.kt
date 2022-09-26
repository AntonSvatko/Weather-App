package com.test.weather.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.weather.data.database.AppDatabase
import com.test.weather.data.database.dao.CityDao
import com.test.weather.data.entity.City
import com.test.weather.di.qualifier.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    const val FILE_NAME = "city_list.json"
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: Callback
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "giphy_app.db")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
        @ApplicationContext private val context: Context
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

        private fun readJSONFromAssets(): String =
            context.assets
                ?.open(FILE_NAME)
                ?.readBytes()
                ?.let(::String)
                .orEmpty()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase) = appDatabase.cityDao

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}