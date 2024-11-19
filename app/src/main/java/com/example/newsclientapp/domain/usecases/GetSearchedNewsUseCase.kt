package com.example.newsclientapp.domain.usecases

import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.domain.repositories.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository){

    suspend fun execute(searchQuery:String):Resource<APIResponse>{
        return newsRepository.getSearchedNews(searchQuery)
    }
}