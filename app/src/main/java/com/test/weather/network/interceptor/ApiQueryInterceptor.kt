package com.test.weather.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiQueryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
//            .addQueryParameter("api_key", ApiConstants.API_KEY)
            .build()


        val request = Request.Builder().url(url).build()
        Log.d("api1", request.url.toString())
        return chain.proceed(request)
    }

}