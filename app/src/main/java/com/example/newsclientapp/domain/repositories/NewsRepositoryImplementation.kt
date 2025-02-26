package com.example.newsclientapp.domain.repositories

import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.domain.repositories.datasource.NewsLocalDatasource
import com.example.newsclientapp.domain.repositories.datasource.NewsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImplementation(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDatasource: NewsLocalDatasource
) :NewsRepository{
    override suspend fun getNewsHeadLines(country:String,page:Int): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getTopHeadLines(country,page))
    }

    override suspend fun getSearchedNews(country: String, searchQuery: String, page: Int): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getSearchedNews(country,searchQuery,page))
    }

    private fun responseToResource(response: Response<APIResponse>):Resource<APIResponse>{
        if (response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }



    override suspend fun saveNews(article: Article) {
        newsLocalDatasource.saveArticleToDb(article)
    }

    override suspend fun deleteNews(article: Article) {
        newsLocalDatasource.deleteArticleFromDB(article)
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return newsLocalDatasource.getSavedArticles()
    }
}