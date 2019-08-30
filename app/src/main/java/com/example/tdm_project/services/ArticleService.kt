package com.example.tdm_project.services

import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.ArticlePost
import com.example.tdm_project.model.data.UserPost
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ArticleService {

    @GET ("/articles")
    fun loadAllArticles(): Call<List<Article>>

    //Save User
    @POST("/users")
    fun saveUser(@Body user : UserPost) : Call<UserPost>

    //Save Article by UserId
    @POST("/articles/save/{userId}")
    fun saveArticle(@Path( "userID" ) userID: String , @Body article : ArticlePost) : Call<ArticlePost>

    //Unsave Article
    @DELETE("/articles/delete/{userID}")
    fun unsaveArticle(@Path( "userID" ) userID: String ,@Query("articleId") articleId:String ) : Call<ResponseBody>



    //Get Saved Article
    @GET ("/articles/{userID}")
    fun getSavedArticles (@Path( "userID" ) userID: String ): Call<List<ArticlePost>>
}