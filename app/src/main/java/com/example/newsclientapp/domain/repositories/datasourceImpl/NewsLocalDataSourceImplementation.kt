package com.example.newsclientapp.domain.repositories.datasourceImpl

import com.example.newsclientapp.data.db.ArticleDAO
import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.domain.repositories.datasource.NewsLocalDatasource
import kotlinx.coroutines.flow.Flow

class NewsLocalDataSourceImplementation(private val articleDAO: ArticleDAO):NewsLocalDatasource{
    override suspend fun saveArticleToDb(article: Article) {
        articleDAO.insert(article)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDAO.getAllArticles()
    }

    override suspend fun deleteArticleFromDB(article: Article) {
        return articleDAO.deleteArticle(article)
    }
}