package com.test.weather.ui.viewmodel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.weather.data.database.dao.CityDao
import com.test.weather.data.entity.City

class UserPostPagingSource(private val userPostDao: CityDao) : PagingSource<Int, City>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, City> {
        val randomPosts = userPostDao.getCities()
        return randomPosts.load(params)
    }

    override fun getRefreshKey(state: PagingState<Int, City>): Int? = null
}