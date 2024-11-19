package com.example.newsclientapp.domain.repositories.datasource

import com.example.newsclientapp.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadLines(country:String,page:Int): Response<APIResponse>
}