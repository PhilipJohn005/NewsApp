package com.example.newsclientapp.presentation.di

import com.example.newsclientapp.domain.repositories.NewsRepository
import com.example.newsclientapp.domain.repositories.NewsRepositoryImplementation
import com.example.newsclientapp.domain.repositories.datasource.NewsLocalDatasource
import com.example.newsclientapp.domain.repositories.datasource.NewsRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NewsRepositoryDependency {

    @Provides
    @Singleton
    fun provideNewsRepositoryDependency(newsRemoteDataSource: NewsRemoteDataSource,newsLocalDatasource: NewsLocalDatasource):NewsRepository{
        return NewsRepositoryImplementation(newsRemoteDataSource,newsLocalDatasource)
    }
}