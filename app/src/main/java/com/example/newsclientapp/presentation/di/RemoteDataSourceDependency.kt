package com.example.newsclientapp.presentation.di

import com.example.newsclientapp.data.api.NewsAPIService
import com.example.newsclientapp.domain.repositories.datasource.NewsRemoteDataSource
import com.example.newsclientapp.domain.repositories.datasourceImpl.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceDependency {

    @Provides
    @Singleton
    fun provideNewsRemoteDataSourceDependency(newsAPIService: NewsAPIService):NewsRemoteDataSource{
        return NewsRemoteDataSourceImpl(newsAPIService)
    }
}