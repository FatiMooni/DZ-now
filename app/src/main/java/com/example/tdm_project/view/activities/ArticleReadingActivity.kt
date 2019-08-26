package com.example.tdm_project.view.activities

import android.annotation.SuppressLint
import android.content.Context

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.tdm_project.R
import com.example.tdm_project.R.string.publish_date
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.sharedPreferences.MyContextWrapper
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_article_reading.*

class ArticleReadingActivity : CustomBaseActivity() {

    lateinit var fabSettings : FloatingActionButton
    lateinit var myPreference: PreferencesProvider
    lateinit var layoutFabSave : LinearLayout
    lateinit var layoutFabShare : LinearLayout
    lateinit var layoutFabSharePrl :  LinearLayout
    lateinit var article :  Article


    var fabExpanded = true
    @SuppressLint("SetTextI18n")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_reading)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        toolbar.setNavigationOnClickListener{
            finish()
        }

       intent?.let {
           article = intent.extras.getParcelable("article") as Article
           Toast.makeText(this,article.title + "" + article.author,Toast.LENGTH_LONG).show()
       }

        findViewById<AppCompatTextView>(R.id.title_input).text =  article.title
        findViewById<AppCompatTextView>(R.id.date_input).text = getString(publish_date) + " " +article.date
        findViewById<AppCompatTextView>(R.id.writer_input).text = article.author
        findViewById<AppCompatTextView>(R.id.article_text).text = article.uri
       val img= findViewById<AppCompatImageView>(R.id.article_image)
        Picasso
            .get() // give it the context
            .load(article.img)
            .into(img)


       fabSettings = this.findViewById(R.id.fabSettings) as FloatingActionButton

         layoutFabSave = this.findViewById(R.id.layoutFabSave) as LinearLayout
         layoutFabShare = this.findViewById(R.id.layoutFabShare) as LinearLayout
         layoutFabSharePrl = this.findViewById(R.id.layoutFabSharePrl) as LinearLayout

        //When main Fab (Settings) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
        fabSettings.setOnClickListener {
            if (fabExpanded) {
                closeSubMenusFab()
            } else {
                openSubMenusFab()
            }
        }

        layoutFabSave.setOnClickListener {
            SharedSavedNews.savePost(ArticleViewModel( article),this)
        }

        layoutFabShare.setOnClickListener {
          SharedSavedNews.sharePost(ArticleViewModel(article),this)
        }

        layoutFabSharePrl.setOnClickListener {
            SharedSavedNews.shareProfilePost(ArticleViewModel(article),this)
        }

        //Only main FAB is visible in the beginning
        closeSubMenusFab()

    }
    private fun closeSubMenusFab(){
        layoutFabSave.visibility = View.INVISIBLE
        layoutFabShare.visibility = View.INVISIBLE
        layoutFabSharePrl.visibility = View.INVISIBLE

        fabSettings.setImageResource(R.drawable.ic_settings_black_24dp)
        fabExpanded = false
    }

    //Opens FAB submenus
    private fun openSubMenusFab(){
        layoutFabSave.visibility = View.VISIBLE
        layoutFabShare.visibility = View.VISIBLE
        layoutFabSharePrl.visibility = View.VISIBLE
        //Change settings icon to 'X' icon
        fabSettings.setImageResource(R.drawable.ic_close_black_24dp)
        fabExpanded = true

    }
    override fun attachBaseContext(newBase: Context?) {
        myPreference = PreferencesProvider(newBase!!)
        val lang = myPreference.getLoginCount()
        super.attachBaseContext(MyContextWrapper.wrap(newBase,lang))
    }

}
