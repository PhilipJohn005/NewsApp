package com.example.newsclientapp.domain.usecases

import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.domain.repositories.NewsRepository

class GetNewsHeadLineUseCase(private val newsRepository: NewsRepository){

    suspend fun execute(country:String,page:Int): Resource<APIResponse>{
        return newsRepository.getNewsHeadLines(country,page)
    }
}