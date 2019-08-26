package com.example.tdm_project.model.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.view.activities.WebBrowserActivity
import com.example.tdm_project.viewmodel.ArticleViewModel

class SharedSavedNews {

    companion object {
       val a = Article ("5748","Le calendrier 2019/2020 de Premier League dévoilé - Foot Mercato",
           "Footmercato.net","2019-12-04","sport",
           "Sacré champion d’Angleterre pour la deuxième fois consécutive, Manchester City est prêt à repartir à la bataille." +
                   " La Premier League vient de dévoiler le calendrier pour la saison 2019/2020. Et voici les dates principales" +
                   ".\nLes 20 clubs de Premier League (...)",
           "http://www.footmercato.net/premier-league/le-calemier-league-devoile_256383",
           "http://www.footmercato.net/images/a/jurgen-klopp-et-pep-guardiola-lors-de-la-saison-2018-201ndrier-2019-2020-de-pre9_256383.jpg")
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