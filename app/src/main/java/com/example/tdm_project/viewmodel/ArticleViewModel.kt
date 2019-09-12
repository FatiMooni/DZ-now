package com.example.tdm_project.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.ArticlePost
import com.example.tdm_project.services.ArticleService
import com.example.tdm_project.services.ServiceBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ArticleViewModel : ViewModel {

    //lists
    private var articleMList = MutableLiveData<ArrayList<ArticleViewModel>>()
    private var articleInnerList = ArrayList<ArticleViewModel>()

    //parameteres
    var _id : String? = ""
    var title: String = ""
    var resume : String? = ""
    var author : String = ""
    var uri : String? = ""
    var img: String? = ""
    var categoryId: String = ""
    var categoryOrigin : String = "Default"
    var fetchDate: Date = Date()
    var publicationDate: Date = fetchDate
    var mobilizedContent: String? = null //in case i want to save my article
    var isRead: Boolean = false //TODO(do i need to handle this)
    var isSavedOffline : Boolean = false

    constructor(
     article: Article
    ) : super() {
        this._id = article._id
        this.title = article.title
        this.resume = article.resume
        this.categoryOrigin = article.categoryOrigin
        this.author = article.author
        this.fetchDate = article.fetchDate
        this.uri = article.uri
        this.img = article.img
        this.isRead = article.isRead
        this.isSavedOffline = article.isSavedOffline
        this.mobilizedContent = article.mobilizedContent
        this.categoryId = article.categoryId
    }

    constructor() : super()

    //to observe my list
    fun getArticles() : MutableLiveData<ArrayList<ArticleViewModel>>{

        articleMList.value = articleInnerList

        return articleMList
    }

    //retrieve data from back end
    fun getData() {
        val service = ServiceBuilder.buildService(ArticleService::class.java)
        val request = service.loadAllArticles()

        request.enqueue(object : Callback<List<Article>> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                Log.e("article fetch", "couldnt get the articles", t)
            }

            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                if (response.isSuccessful){
                    response.body()!!.forEach {
                        articleInnerList.add(ArticleViewModel(it))
                    }

                    articleMList.value = articleInnerList
                }
            }

        })
    }

    ///GetSavedArticle from backEnd

    fun getSavedArticle(userId : String) {
        val service = ServiceBuilder.buildService(ArticleService::class.java)
        val request = service.getSavedArticles(userId)

        request.enqueue(object : Callback<List<ArticlePost>> {
            override fun onFailure(call: Call<List<ArticlePost>>, t: Throwable) {
                Log.e("GETSAVEDFAIL", "couldnt get the articles", t)
            }


            override fun onResponse(call: Call<List<ArticlePost>>, response: Response<List<ArticlePost>>) {
                if (response.isSuccessful){
                    response.body()!!.forEach {

                        Log.i("GETSAVED",it.title)
                        // articleInnerList.add(ArticleViewModel(it))
                    }


                    //articleMList.value = articleInnerList
                }
            }

        })
    }

    //Delete unsave article
    fun unsaveArticle ( userId :String,uri:String) {

        val service = ServiceBuilder.buildService(ArticleService::class.java)
        val request = service.unsaveArticle(userId,uri)

        request.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("DELETEFAIL", "couldnt delete", t)
            }


            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){


                        Log.i("DELETED","YOUPII")
                        // articleInnerList.add(ArticleViewModel(it))
                    }


                    //articleMList.value = articleInnerList
                }

        })





    }
}