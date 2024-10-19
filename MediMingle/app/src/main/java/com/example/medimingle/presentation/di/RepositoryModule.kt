package com.example.medimingle.presentation.di

import android.app.Application
import android.content.Context
import com.example.medimingle.data.repository.TaskCategoryRepositoryImpl
import com.example.medimingle.data.util.SharedPreferencesManager
import com.example.medimingle.domain.TaskCategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(app: Application): SharedPreferencesManager {
        return SharedPreferencesManager(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideTaskCategoryRepository(
        sharedPreferencesManager: SharedPreferencesManager
    ): TaskCategoryRepository {
        return TaskCategoryRepositoryImpl(sharedPreferencesManager)
    }
}
