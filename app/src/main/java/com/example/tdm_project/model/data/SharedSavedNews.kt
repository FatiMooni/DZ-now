package com.example.tdm_project.model.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.view.activities.WebBrowserActivity
import com.example.tdm_project.viewmodel.ArticleViewModel
import java.util.*
import kotlin.collections.ArrayList

class SharedSavedNews {

    companion object {
       val a = Article ()
        var sharedPosts = arrayListOf<ArticleViewModel>( ArticleViewModel(a))
        fun getListSharedPosts() : ArrayList<ArticleViewModel> {
            return  sharedPosts
        }

        fun sharePost( item : ArticleViewModel , c : Context)
        {
            var myIntent = Intent (Intent.ACTION_SEND)
            myIntent.setType("text/plain")
            myIntent.putExtra(Intent.EXTRA_SUBJECT,item.title)
            myIntent.putExtra(Intent.EXTRA_TEXT,item.uri)
            myIntent.putExtra(Intent.EXTRA_TITLE,item.resume)
            c.startActivity(Intent.createChooser(myIntent,c.getResources().getString(R.string.share)))
        }
        fun shareProfilePost(item : ArticleViewModel , c : Context)
        {
            if ( !sharedPosts.contains(item))  {sharedPosts.add(item)
                                               Toast.makeText(c, "news partagé avec succés ", Toast.LENGTH_SHORT).show() }
            else  Toast.makeText(c, "news déja partagé dans votre profile", Toast.LENGTH_SHORT).show()
        }
        fun savePost(item : ArticleViewModel , c : Context){
            if ( !savedPosts.contains(item))  {savedPosts.add(item)
                Toast.makeText(c, "news enregistré avec succés", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(c, "news déja enregistré", Toast.LENGTH_SHORT).show()
        }

        var savedPosts = arrayListOf<ArticleViewModel>( ArticleViewModel(a))
        fun getListSavedPosts() : ArrayList<ArticleViewModel> {
            return  savedPosts
        }

        fun readArticle(item: Article, context: Context) {
            val intent = Intent(context, WebBrowserActivity::class.java)
                intent.putExtra("article",item)
                context.startActivity(intent)

        }


    }

}