package com.test.weather.di.module

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.weather.di.qualifier.ApiOkHttpClient
import com.test.weather.network.api.GifService
import com.test.weather.network.const.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GifService {
        Log.d(
            "retrofit1", retrofit.baseUrl().query.toString()
        )
        return retrofit.create(GifService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @ApiOkHttpClient okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {

        val retrofit = Retrofit.Builder().baseUrl(ApiConstants.BASE_PHOTO_LINK).client(okHttpClient)
            .addConverterFactory(converterFactory).build()
        Log.d(
            "retrofit1", retrofit.baseUrl().query.toString()
        )
        return retrofit
    }

    @Provides
    @Singleton
    fun provideGsonFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


}