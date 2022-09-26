package com.test.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.weather.data.database.dao.CityDao
import com.test.weather.data.entity.City

@Database(entities = [City::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val cityDao: CityDao
}