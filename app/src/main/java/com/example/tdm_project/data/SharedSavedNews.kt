package com.example.tdm_project.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.tdm_project.ArticleReadingActivity
import com.example.tdm_project.R
import com.example.tdm_project.sharedPreferences.WebBrowserActivity

class SharedSavedNews {

    companion object {

       var sharedPosts = arrayListOf<news>( news ("Le calendrier 2019/2020 de Premier League dévoilé - Foot Mercato","Footmercato.net","2019-12-04",
           "Sacré champion d’Angleterre pour la deuxième fois consécutive, Manchester City est prêt à repartir à la bataille. La Premier League vient de dévoiler le calendrier pour la saison 2019/2020. Et voici les dates principales.\nLes 20 clubs de Premier League (...)",
           "http://www.footmercato.net/premier-league/le-calendrier-2019-2020-de-premier-league-devoile_256383","http://www.footmercato.net/images/a/jurgen-klopp-et-pep-guardiola-lors-de-la-saison-2018-2019_256383.jpg","sport"))

        fun getListSharedPosts() : ArrayList<news> {
            return  sharedPosts
        }

        fun sharePost( item : news , c : Context)
        {
            var myIntent = Intent (Intent.ACTION_SEND)
            myIntent.setType("text/plain")
            myIntent.putExtra(Intent.EXTRA_SUBJECT,item.Title)
            myIntent.putExtra(Intent.EXTRA_TEXT,item.url)
            myIntent.putExtra(Intent.EXTRA_TITLE,item.Second_title)
            c.startActivity(Intent.createChooser(myIntent,c.getResources().getString(R.string.share)))
        }
        fun shareProfilePost(item : news , c : Context)
        {
            if ( !sharedPosts.contains(item))  {sharedPosts.add(item)
                                               Toast.makeText(c, "news partagé avec succés ", Toast.LENGTH_SHORT).show() }
            else  Toast.makeText(c, "news déja partagé dans votre profile", Toast.LENGTH_SHORT).show()
        }
        fun savePost(item : news , c : Context){
            if ( !savedPosts.contains(item))  {savedPosts.add(item)
                Toast.makeText(c, "news enregistré avec succés", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(c, "news déja enregistré", Toast.LENGTH_SHORT).show()
        }

        var savedPosts = arrayListOf<news>( news ("Le calendrier 2019/2020 de Premier League dévoilé - Foot Mercato","Footmercato.net","2019-12-04",
            "Sacré champion d’Angleterre pour la deuxième fois consécutive, Manchester City est prêt à repartir à la bataille. La Premier League vient de dévoiler le calendrier pour la saison 2019/2020. Et voici les dates principales.\nLes 20 clubs de Premier League (...)",
            "http://www.footmercato.net/premier-league/le-calendrier-2019-2020-de-premier-league-devoile_256383","http://www.footmercato.net/images/a/jurgen-klopp-et-pep-guardiola-lors-de-la-saison-2018-2019_256383.jpg","sport"))

        fun getListSavedPosts() : ArrayList<news> {
            return  savedPosts
        }

        fun readArticle(item: news, context: Context) {
            val intent = Intent(context, WebBrowserActivity::class.java)
                intent.putExtra("article",item)
                context.startActivity(intent)

        }


    }

}