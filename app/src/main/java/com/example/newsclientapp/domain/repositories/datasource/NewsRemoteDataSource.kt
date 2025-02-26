package com.example.newsclientapp.domain.repositories.datasource

import com.example.newsclientapp.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadLines(country:String,page:Int): Response<APIResponse>
    suspend fun getSearchedNews(country:String,searchQuery:String,page:Int): Response<APIResponse>
}