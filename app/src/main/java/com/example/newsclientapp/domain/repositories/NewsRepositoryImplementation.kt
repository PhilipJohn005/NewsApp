package com.example.newsclientapp.domain.repositories

import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.domain.repositories.datasource.NewsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImplementation(
    private val newsRemoteDataSource: NewsRemoteDataSource
) :NewsRepository{
    override suspend fun getNewsHeadLines(country:String,page:Int): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getTopHeadLines(country,page))
    }

    private fun responseToResource(response: Response<APIResponse>):Resource<APIResponse>{
        if (response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getSearchedNews(searchQuery: String): Resource<APIResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNews(article: Article) {

    }

    override suspend fun deleteNews(article: Article) {

    }

    override fun getSavedNews(): Flow<List<Article>> {
        TODO("Not yet implemented")
    }
}