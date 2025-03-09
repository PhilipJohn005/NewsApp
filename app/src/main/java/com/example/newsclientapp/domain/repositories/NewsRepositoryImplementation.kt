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

    private fun responseToResource(response: Response<APIResponse>): Resource<APIResponse> {
        return when {
            response.isSuccessful -> {
                response.body()?.let { result ->
                    Resource.Success(result)
                } ?: Resource.Error("No data available")
            }
            response.code() == 404 -> Resource.Error("Error 404: Not found")
            response.code() == 500 -> Resource.Error("Error 500: Server error")
            response.code() == 401 -> Resource.Error("Unauthorized access")
            response.code() == 403 -> Resource.Error("Forbidden access")
            else -> Resource.Error("Unexpected error: ${response.message()}")
        }
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