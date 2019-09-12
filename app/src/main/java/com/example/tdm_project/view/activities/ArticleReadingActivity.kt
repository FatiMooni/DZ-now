package com.example.tdm_project.view.activities

import android.os.Bundle
import android.widget.Toast
import com.example.tdm_project.R
import com.example.tdm_project.model.Article

class ArticleReadingActivity : CustomBaseActivity() {
    lateinit var article: Article
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_reading)
        //Get a reference to your WebView//
        val webView = findViewById<ArticleReadingView>(R.id.customwebview)
        if (intent.hasExtra("article"))
            intent?.let {
                article = intent.extras!!.getParcelable("article") as Article
                Toast.makeText(this, article.title + "" + article.author, Toast.LENGTH_LONG).show()
                webView.setArticle(article)
            }


    }
}