package com.example.newsclientapp.presentation.di

import com.example.newsclientapp.data.db.ArticleDAO
import com.example.newsclientapp.domain.repositories.datasource.NewsLocalDatasource
import com.example.newsclientapp.domain.repositories.datasourceImpl.NewsLocalDataSourceImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(articleDAO: ArticleDAO): NewsLocalDatasource {
        return NewsLocalDataSourceImplementation(articleDAO)
    }
}