package com.example.tdm_project.sharedPreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.example.tdm_project.R
import com.example.tdm_project.data.news


class WebBrowserActivity : AppCompatActivity() {
    lateinit var article : news
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_browser)
        //Get a reference to your WebView//
        val webView = findViewById(R.id.webview) as WebView
        intent?.let {
            article = intent.extras.getParcelable("article") as news
            Toast.makeText(this,article.Title + "" + article.Writer, Toast.LENGTH_LONG).show()
        }

        //Specify the URL you want to display//
        webView.loadUrl(article.url)

    }

}
