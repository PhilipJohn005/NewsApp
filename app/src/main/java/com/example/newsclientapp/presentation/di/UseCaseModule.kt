package com.example.newsclientapp.presentation.di

import com.example.newsclientapp.domain.repositories.NewsRepository
import com.example.newsclientapp.domain.usecases.GetNewsHeadLineUseCase
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
}