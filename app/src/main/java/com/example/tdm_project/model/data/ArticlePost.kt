package com.example.tdm_project.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class  ArticlePost (
    @SerializedName("articleId")
    @Expose
    var articleId : String ,
    @SerializedName("userId")
    @Expose
    var userId : String ,
    @SerializedName("title")
    @Expose
    var title : String ,
    @SerializedName("image")
    @Expose
    var image: String

)