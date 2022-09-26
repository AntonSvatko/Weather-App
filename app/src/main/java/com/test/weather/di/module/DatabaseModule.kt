package com.test.weather.di.module

import android.content.Context
import androidx.room.Room
import com.test.weather.data.database.AppDatabase
import com.test.weather.di.qualifier.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: AppDatabase.Callback
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "giphy_app.db")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase) = appDatabase.cityDao

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}