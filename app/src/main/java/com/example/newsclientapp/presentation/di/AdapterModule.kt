package com.example.newsclientapp.presentation.di

import com.example.newsclientapp.presentation.adapter.NewsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Provides
    fun provideNewsAdapter(): NewsAdapter {
        return NewsAdapter()
    }
}