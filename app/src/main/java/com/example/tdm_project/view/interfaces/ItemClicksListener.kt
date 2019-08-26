package com.example.tdm_project.view.interfaces

import android.view.View
import com.example.tdm_project.viewmodel.ArticleViewModel

interface ItemClicksListener {
    fun onPopupRequested(view: View, article: ArticleViewModel, position: Int)
    fun onItemClick(article: ArticleViewModel, position: Int)
}