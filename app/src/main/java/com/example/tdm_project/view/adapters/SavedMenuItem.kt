package com.example.tdm_project.view.adapters

import android.content.Context
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import com.example.tdm_project.R
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.data.ArticlePost
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.services.ArticleService
import com.example.tdm_project.services.CategoryService
import com.example.tdm_project.services.ServiceBuilder
import com.example.tdm_project.view.fragments.HomeFragment
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.facebook.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedMenuItem (private val it: ArticleViewModel, private val cont: Context) : PopupMenu.OnMenuItemClickListener {
    var userId = Profile.getCurrentProfile().id
    var article = ArticlePost(it._id!!,userId,it.title,it.uri!!,it.categoryId,it.categoryOrigin,it.publicationDate.toString(),it.author)
    override fun onMenuItemClick(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {

            R.id.btn_shareSaved -> {



                //To Implement

                return true
            }

            R.id.btn_signet -> {
                Log.i("ART",it._id)
                Log.i("ART",it.title)
                Log.i("ART",it.uri)
                Log.i("ART",it.categoryId)

                Log.i("SIGNET","SIGN")

                //Send to the back end
                val service = ServiceBuilder.buildService(ArticleService::class.java)
                val request = service.saveArticle(userId,article)

                request.enqueue(object : Callback<ArticlePost> {

                    override fun onFailure(call: Call<ArticlePost>, t: Throwable) {
                        Log.e("SEND", "UNDONE",t)
                    }

                    override fun onResponse(call: Call<ArticlePost>, response: Response<ArticlePost>) {
                        if (response.isSuccessful) {
                            Log.e("SEND", "DONE")
                            }
                        }
                    })


                return true
            }

        }
        return false
    }
}


