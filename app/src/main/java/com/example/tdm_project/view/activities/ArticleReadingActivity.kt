package com.example.tdm_project.view.activities

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.getMaxSpeechInputLength
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.sharedPreferences.MyContextWrapper
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_article_reading.*
import java.util.*


class ArticleReadingActivity : CustomBaseActivity() {

    lateinit var fabSettings: FloatingActionButton
    lateinit var myPreference: PreferencesProvider
    lateinit var layoutFabSave: LinearLayout
    lateinit var layoutFabShare: LinearLayout
    lateinit var layoutFabSharePrl: LinearLayout
    lateinit var textSpeech: TextToSpeech
    lateinit var article: Article

    private val HTML_IMG_REGEX = "(?i)<[/]?[ ]?img(.|\n)*?>"


    var fabExpanded = true
    var radyToread = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_reading)
        setSupportActionBar(toolbar)

        //Get a reference to your WebView//
        val webView = findViewById<ArticleReadingView>(R.id.customwebview)
        if (intent.hasExtra("article"))
            intent?.let {
                article = intent.extras!!.getParcelable("article") as Article
                //Toast.makeText(this, article.title + "" + article.author, Toast.LENGTH_LONG).show()
                webView.setArticle(article)
            }

        initArticleReader()
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
            SharedSavedNews.savePost(ArticleViewModel(article), this)
        }

        layoutFabShare.setOnClickListener {
            SharedSavedNews.sharePost(ArticleViewModel(article), this)
        }

        layoutFabSharePrl.setOnClickListener {
            SharedSavedNews.shareProfilePost(ArticleViewModel(article), this)
        }

        //Only main FAB is visible in the beginning
        closeSubMenusFab()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // ajouter le menu au top de l'activité

        menuInflater.inflate(R.menu.article_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        /* if(HomeFragment.verticallayout) find<RadioButton>(R.id.vert_disp).isChecked = true
         else find<RadioButton>(R.id.hori_disp).isChecked = true*/
        when (item!!.itemId) {
            R.id.read_article -> ReadArticle(article)

        }
        return true
    }

    private fun closeSubMenusFab() {
        layoutFabSave.visibility = View.INVISIBLE
        layoutFabShare.visibility = View.INVISIBLE
        layoutFabSharePrl.visibility = View.INVISIBLE

        fabSettings.setImageResource(R.drawable.ic_settings_black_24dp)
        fabExpanded = false
    }

    //Opens FAB submenus
    private fun openSubMenusFab() {
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
        super.attachBaseContext(MyContextWrapper.wrap(newBase, lang))
    }

    private fun initArticleReader() {
        textSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show()
                } else radyToread = true
            } else {
                Toast.makeText(this, "Failed to initialize the text to speech", Toast.LENGTH_LONG)
                    .show()

            }

        })
    }

    private fun ReadArticle(article: Article) {
        var list: List<String>
        textSpeech.setSpeechRate(1f)
        textSpeech.setPitch(1f)
        var contentText =
            if (article.mobilizedContent.isNotBlank() && article.mobilizedContent.isNotEmpty()) article.mobilizedContent
            else article.resume!!
        contentText = contentText.replace(HTML_IMG_REGEX.toRegex(), "")
        contentText =
            contentText.replace(("<(.*?)\\>").toRegex(), "")//Removes all items in brackets
        contentText = contentText.replace(("<(.*?)\\\n").toRegex(), "")//Must be undeneath
        contentText = contentText.replaceFirst(
            ("(.*?)\\>").toRegex(),
            ""
        )//Removes any connected item to the last bracket
        contentText = contentText.replace("&nbsp;", "")
        contentText = contentText.replace("&amp;", "")
        val length = getMaxSpeechInputLength() - 1

        list = contentText.chunked(length)


            //Toast.makeText(this, list[0].length.toString() + " " + getMaxSpeechInputLength() , Toast.LENGTH_LONG).show()
            textSpeech.speak(article.resume, TextToSpeech.QUEUE_FLUSH, null)


    }

    fun String.chunked(size: Int): List<String> {
        val nChunks = length / size
        return (0 until nChunks).map { substring(it * size, (it + 1) * size) }
    }

    override fun onDestroy() {
        if (textSpeech != null) {
            textSpeech.stop()
            textSpeech.shutdown()
        }
        super.onDestroy()
    }
}