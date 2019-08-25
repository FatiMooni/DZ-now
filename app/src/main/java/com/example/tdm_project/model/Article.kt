package com.example.tdm_project.model

data class Article (val _id : String,
                    val title: String,
                    val resume : String?,
                    val theme : String,
                    val author : String,
                    val date : String,
                    val uri : String,
                    val img: String)