package com.example.newsclientapp.domain.repositories.datasourceImpl

import com.example.newsclientapp.data.api.NewsAPIService
import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.domain.repositories.datasource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService
):NewsRemoteDataSource{
    override suspend fun getTopHeadLines(country: String,page: Int): Response<APIResponse> {
        return newsAPIService.getTopHeadlines(country,page)
    }

    override suspend fun getSearchedNews(
        country: String,
        searchQuery: String,
        page: Int
    ): Response<APIResponse> {
        return newsAPIService.getSearchedTopHeadlines(country,searchQuery,page)
    }
}