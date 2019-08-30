package com.example.tdm_project.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.news
import com.example.tdm_project.viewmodel.ArticleViewModel


class WebBrowserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URI = "com.example.tdm_project.view.activities.WebBrowserActivity.uri"
    }
    lateinit var article : Article
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_browser)
        //Get a reference to your WebView//
        val webView = findViewById<WebView>(R.id.webview)
        if (intent.hasExtra("article"))
        intent?.let {
            article = intent.extras!!.getParcelable("article") as Article
            Toast.makeText(this,article.title + "" + article.author, Toast.LENGTH_LONG).show()

            //Specify the URL you want to display//
            webView.loadUrl(article.uri)
        }
        if(intent.hasExtra(EXTRA_URI)){
            val uri = intent.getStringExtra(EXTRA_URI)
            Toast.makeText(this,uri, Toast.LENGTH_LONG).show()
            //Specify the URL you want to display//
            webView.loadUrl(uri)
        }



    }

}
