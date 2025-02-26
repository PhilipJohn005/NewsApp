package com.example.newsclientapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsclientapp.domain.usecases.DeleteSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetNewsHeadLineUseCase
import com.example.newsclientapp.domain.usecases.GetSavedNewsUseCase
import com.example.newsclientapp.domain.usecases.GetSearchedNewsUseCase
import com.example.newsclientapp.domain.usecases.SaveNewsUseCase

class NewsViewModelFactory(
    private val app:Application,
    private val getNewsHeadLineUseCase: GetNewsHeadLineUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val saveNewsUseCase:SaveNewsUseCase,
    private val getSavedNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(
            app,
            getNewsHeadLineUseCase,
            getSearchedNewsUseCase,
            saveNewsUseCase,
            getSavedNewsUseCase,
            deleteSavedNewsUseCase
        ) as T
    }
}