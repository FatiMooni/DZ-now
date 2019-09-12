package com.example.tdm_project.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class  ArticlePost (
    @SerializedName("articleID")
    @Expose
    var articleId : String ,
    @SerializedName("userID")
    @Expose
    var userId : String ,
    @SerializedName("title")
    @Expose
    var title : String ,
    @SerializedName("uri")
    @Expose
    var uri : String ,
    @SerializedName("categoryID")
    @Expose
    var categoryId : String,
    @SerializedName("categoryOrigin")
    @Expose
    var categoryOrigin : String,
    @SerializedName("publicationDate")
    @Expose
    var publicationDate: String ,
    @SerializedName("author")
    @Expose
    var author : String


)