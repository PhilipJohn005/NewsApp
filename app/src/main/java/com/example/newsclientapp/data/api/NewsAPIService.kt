package com.example.newsclientapp.data.api

import com.example.newsclientapp.BuildConfig
import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.util.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country:String,
        @Query("page")
        page:Int,
        @Query("apiKey")
        apiKey:String=BuildConfig.API_KEY
    ): Response<APIResponse>

    @GET("v2/top-headlines")
    suspend fun getSearchedTopHeadlines(
        @Query("country")
        country:String,
        @Query("q")
        searchQuery: String,
        @Query("page")
        page:Int,
        @Query("apiKey")
        apiKey:String=BuildConfig.API_KEY
    ): Response<APIResponse>

}