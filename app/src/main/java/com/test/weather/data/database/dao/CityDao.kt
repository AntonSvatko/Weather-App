package com.test.weather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.weather.data.entity.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert
    suspend fun insert(list: List<City>)

    @Query("SELECT * FROM city_table")
    fun getCities(): Flow<List<City>>

    @Query("SELECT * FROM city_table WHERE name LIKE '%' || :text  || '%'")
    fun getSearchedCities(text: String): Flow<List<City>>
}