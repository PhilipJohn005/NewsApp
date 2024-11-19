package com.example.newsclientapp.presentation.di

import android.app.Application
import com.example.newsclientapp.domain.usecases.GetNewsHeadLineUseCase
import com.example.newsclientapp.presentation.viewmodel.NewsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ViewModelFactoryModule {


    @Provides
    @Singleton
    fun provideNewsViewModelFactory(application: Application,getNewsHeadLineUseCase: GetNewsHeadLineUseCase): NewsViewModelFactory {
        return NewsViewModelFactory(application,getNewsHeadLineUseCase)
    }
}