package com.example.newsclientapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.model.Article
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.domain.usecases.DeleteSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetNewsHeadLineUseCase
import com.example.newsclientapp.domain.usecases.GetSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetSearchedNewsUseCase
import com.example.newsclientapp.domain.usecases.SaveNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//to pass application context as argument for isNetworkAvailable we need to extend AndroidviewModel and not ViewModel
class NewsViewModel(
    val app:Application, //application context as argument
    val getNewsHeadLineUseCase: GetNewsHeadLineUseCase,
    val getSearchedNewsUseCase:GetSearchedNewsUseCase,
    val saveNewsUseCase:SaveNewsUseCase,
    val getSavedNewsUseCase: GetSavedNewsUseCase,
    val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
    ): AndroidViewModel(app){

    val newsHeadlines:MutableLiveData<Resource<APIResponse>> = MutableLiveData()

    fun getNewsHeadLines(country:String,page:Int)=viewModelScope.launch(Dispatchers.IO) {
        Log.i("my","getNewsHeadLines")
        newsHeadlines.postValue(Resource.Loading())

        try {
            if (isNetworkAvailable(app)) {//to pass application context as argument for isNetworkAvailable we need to extend AndroidviewModel and not ViewModel
                Log.i("my","Internet is Available")
                val apiResult = getNewsHeadLineUseCase.execute(country,page)
                newsHeadlines.postValue(apiResult)
            } else {
                Log.i("my","Internet is Not Available")
                newsHeadlines.postValue(Resource.Error("Internet is Not Available"))
            }
        }catch (e:Exception){
            newsHeadlines.postValue(Resource.Error(e.message.toString()))
        }
    }



    private fun isNetworkAvailable(context: Context?):Boolean{
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false

    }

    //search

    val searchedNews: MutableLiveData<Resource<APIResponse>> = MutableLiveData()

    fun searchNews(country: String,searchQuery:String,page:Int)=viewModelScope.launch {
        searchedNews.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable(app)) {
                val apiResult = getSearchedNewsUseCase.execute(country, searchQuery, page)
                searchedNews.postValue(apiResult)
            }else{
                searchedNews.postValue(Resource.Error("Internet is Not Available"))
            }
        }catch (Exception:Exception){
            searchedNews.postValue(Resource.Error(Exception.message.toString()))
        }
    }

    //local data
    fun saveArticle(article: Article)=viewModelScope.launch {
        saveNewsUseCase.execute(article)
    }

    fun getSavedNews()= liveData {
        getSavedNewsUseCase.execute().collect{
            emit(it)
        }
    }

    fun deleteArticle(article: Article)=viewModelScope.launch {
        deleteSavedNewsUseCase.execute(article)
    }
}

