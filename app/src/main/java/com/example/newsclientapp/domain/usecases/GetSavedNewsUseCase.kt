package com.example.newsclientapp.domain.usecases

import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.domain.repositories.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetSavedNewsUseCase(private val newsRepository: NewsRepository) {

    fun execute(): Flow<List<Article>>{
        return newsRepository.getSavedNews()
    }
}