package com.example.newsclientapp.presentation.di

import com.example.newsclientapp.domain.repositories.NewsRepository
import com.example.newsclientapp.domain.usecases.DeleteSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetNewsHeadLineUseCase
import com.example.newsclientapp.domain.usecases.GetSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetSearchedNewsUseCase
import com.example.newsclientapp.domain.usecases.SaveNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetNewsHeadLinesUseCase(newsRepository: NewsRepository):GetNewsHeadLineUseCase{
        return GetNewsHeadLineUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchNewsUseCase(newsRepository: NewsRepository):GetSearchedNewsUseCase{
        return GetSearchedNewsUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideSaveNewsUseCase(newsRepository: NewsRepository):SaveNewsUseCase{
        return SaveNewsUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideGetSavedNewsUseCase(newsRepository: NewsRepository):GetSavedNewsUseCase{
        return GetSavedNewsUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSavedNewsUseCase(newsRepository: NewsRepository): DeleteSavedNewsUseCase {
        return DeleteSavedNewsUseCase(newsRepository)
    }
}