package com.example.newsclientapp.domain.repositories.datasource

import com.example.newsclientapp.data.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsLocalDatasource {

    suspend fun saveArticleToDb (article: Article)
    fun getSavedArticles(): Flow<List<Article>>
    suspend fun deleteArticleFromDB(article: Article)
}