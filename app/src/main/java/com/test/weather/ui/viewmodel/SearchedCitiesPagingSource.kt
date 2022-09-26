package com.test.weather.ui.viewmodel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.weather.data.database.dao.CityDao
import com.test.weather.data.entity.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchedCitiesPagingSource(private val text: String,private val userPostDao: CityDao) : PagingSource<Int, City>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, City> =
        withContext(Dispatchers.IO) {
            val position = params.key ?: 0
            val randomPosts = userPostDao.getSearchedCities(text)
            randomPosts.load(params)
        }


    override fun getRefreshKey(state: PagingState<Int, City>): Int? = null
}