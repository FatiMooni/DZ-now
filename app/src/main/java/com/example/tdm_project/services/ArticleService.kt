package com.example.tdm_project.services

import com.example.tdm_project.model.Article
import retrofit2.Call
import retrofit2.http.GET


interface ArticleService {

    @GET ("/articles")
    fun loadAllArticles(): Call<List<Article>>
}