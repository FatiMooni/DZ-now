package com.example.tdm_project.services

import com.example.tdm_project.model.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET

interface CategoryService {

    @GET("/categories")
    fun getAllAvailbleCategoris() : Call<List<Category>>
}