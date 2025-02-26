package com.example.newsclientapp.presentation.di

import android.app.Application
import com.example.newsclientapp.domain.usecases.DeleteSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetNewsHeadLineUseCase
import com.example.newsclientapp.domain.usecases.GetSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetSearchedNewsUseCase
import com.example.newsclientapp.domain.usecases.SaveNewsUseCase
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
    fun provideNewsViewModelFactory(application: Application,getNewsHeadLineUseCase: GetNewsHeadLineUseCase,getSearchedNewsUseCase: GetSearchedNewsUseCase,saveNewsUseCase: SaveNewsUseCase,getSavedNewsUseCase: GetSavedNewsUseCase,deleteSavedNewsUseCase: DeleteSavedNewsUseCase): NewsViewModelFactory {
        return NewsViewModelFactory(application,getNewsHeadLineUseCase,getSearchedNewsUseCase,saveNewsUseCase,getSavedNewsUseCase,deleteSavedNewsUseCase)
    }
}