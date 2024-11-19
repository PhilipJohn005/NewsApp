package com.example.newsclientapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsclientapp.data.model.APIResponse
import com.example.newsclientapp.data.util.Resource
import com.example.newsclientapp.domain.usecases.GetNewsHeadLineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//to pass application context as argument for isNetworkAvailable we need to extend AndroidviewModel and not ViewModel
class NewsViewModel(
    val app:Application,
    val getNewsHeadLineUseCase: GetNewsHeadLineUseCase
): AndroidViewModel(app){

    val newsHeadlines:MutableLiveData<Resource<APIResponse>> = MutableLiveData()

    fun getNewsHeadLines(country:String,page:Int)=viewModelScope.launch(Dispatchers.IO) {

        newsHeadlines.postValue(Resource.Loading())

        try {
            if (isNetworkAvailable(app)) {//to pass application context as argument for isNetworkAvailable we need to extend AndroidviewModel and not ViewModel

                val apiResult = getNewsHeadLineUseCase.execute(country,page)
                newsHeadlines.postValue(apiResult)
            } else {
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
}