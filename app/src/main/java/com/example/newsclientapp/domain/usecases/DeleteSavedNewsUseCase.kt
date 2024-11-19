package com.example.newsclientapp.domain.usecases

import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.domain.repositories.NewsRepository

class DeleteSavedNewsUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(article: Article)=newsRepository.deleteNews(article)
}