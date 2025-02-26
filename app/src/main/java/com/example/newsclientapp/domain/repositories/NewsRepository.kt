package com.example.newsclientapp.domain.repositories

import android.app.DownloadManager.Query
import androidx.lifecycle.LiveData
import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNewsHeadLines(country:String,page:Int):Resource<APIResponse>
    suspend fun getSearchedNews(country: String,searchQuery: String,page: Int):Resource<APIResponse>
    suspend fun saveNews(article: Article)
    suspend fun deleteNews(article: Article)
    fun getSavedNews():Flow<List<Article>>
}