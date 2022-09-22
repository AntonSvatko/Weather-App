package com.test.weather.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET

interface GifService {

    @GET("Temp3.png")
    suspend fun fetchEvenPhoto(): ResponseBody

    @GET("Temp1.png")
    suspend fun fetchOddPhoto(): ResponseBody
}