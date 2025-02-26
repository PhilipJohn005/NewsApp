package com.example.newsclientapp.domain.usecases

import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.domain.repositories.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository){

    suspend fun execute(country:String,searchQuery:String,page:Int):Resource<APIResponse>{
        return newsRepository.getSearchedNews(country,searchQuery,page)
    }
}