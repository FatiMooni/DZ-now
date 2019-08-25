package com.example.tdm_project.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.tdm_project.model.Article

class ArticleViewModelFactory(var article: Article) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  ArticleViewModel(article) as T
    }
}