package com.example.tdm_project.view.activities

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import org.jetbrains.anko.colorAttr

class ArticleReadingView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    WebView(context, attrs, defStyleAttr) {

    private val UTF8 = "UTF-8"
    private val TEXT_HTML = "text/html"
    private val HTML_IMG_REGEX = "(?i)<[/]?[ ]?img(.|\n)*?>"
    private val BACKGROUND_COLOR = colorString(R.attr.background_color)
    private val QUOTE_BACKGROUND_COLOR = colorString(R.attr.background_color)
    private val QUOTE_LEFT_COLOR = colorString(R.attr.secondary_text_color)
    private val TEXT_COLOR = colorString(R.attr.primary_text_color)
    private val SUBTITLE_COLOR = colorString(R.attr.secondary_text_color)
    private val SUBTITLE_BORDER_COLOR = "solid " + colorString(R.attr.colorAccent)
    private val CSS = "<head><style type='text/css'> " +
            "body {max-width: 100%; margin: 0.3cm; font-family: sans-serif-light; color: " + TEXT_COLOR + "; background-color:" + BACKGROUND_COLOR + "; line-height: 150%} " +
            "* {max-width: 100%; word-break: break-word}" +
            "h1, h2 {font-weight: normal; line-height: 130%} " +
            "h1 {font-size: 170%; margin-bottom: 0.1em} " +
            "h2 {font-size: 140%} " +
            "a {color: #0099CC}" +
            "h1 a {color: inherit; text-decoration: none}" +
            "img {height: auto} " +
            "pre {white-space: pre-wrap; direction: ltr;} " +
            "blockquote {border-left: thick solid " + QUOTE_LEFT_COLOR + "; background-color:" + QUOTE_BACKGROUND_COLOR + "; margin: 0.5em 0 0.5em 0em; padding: 0.5em} " +
            "p {margin: 0.8em 0 0.8em 0} " +
            "p.subtitle {color: " + SUBTITLE_COLOR + "; border-top:1px " + SUBTITLE_BORDER_COLOR + "; border-bottom:1px " + SUBTITLE_BORDER_COLOR + "; padding-top:2px; padding-bottom:2px; font-weight:800 } " +
            "ul, ol {margin: 0 0 0.8em 0.6em; padding: 0 0 0 1em} " +
            "ul li, ol li {margin: 0 0 0.8em 0; padding: 0} " +
            "</style><meta name='viewport' content='width=device-width'/></head>"
    private val BODY_START = "<body dir=\"auto\">"
    private val BODY_END = "</body>"
    private val TITLE_START = "<h1><a href='"
    private val TITLE_MIDDLE = "'>"
    private val TITLE_END = "</a></h1>"
    private val SUBTITLE_START = "<p class='subtitle'>"
    private val SUBTITLE_END = "</p>"

    init {

        // For scrolling
        isHorizontalScrollBarEnabled = false
        settings.useWideViewPort = false
        settings.cacheMode = WebSettings.LOAD_NO_CACHE

        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true

        // For color
        setBackgroundColor(Color.parseColor(BACKGROUND_COLOR))

        // Text zoom level from preferences
        val fontSize = 1
        if (fontSize != 0) {
            settings.textZoom = 100 + fontSize * 20
        }

        /* webViewClient = object : WebViewClient() {

             @Suppress("OverridingDeprecatedMember")
             override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                 try {
                     if (url.startsWith(FILE_SCHEME)) {
                         val file = File(url.replace(FILE_SCHEME, ""))
                         val contentUri =
                             FileProvider.getUriForFile(context, "net.frju.flym.fileprovider", file)
                         val intent = Intent(Intent.ACTION_VIEW)
                         intent.setDataAndType(contentUri, "image/jpeg")
                         context.startActivity(intent)
                     } else {
                         val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                         context.startActivity(intent)
                     }
                 } catch (e: ActivityNotFoundException) {
                     Toast.makeText(context, R.string.cant_open_link, Toast.LENGTH_SHORT).show()
                 } catch (e: IOException) {
                     e.printStackTrace()
                 }

                 return true
             }
         }*/
    }

    fun setArticle(article: Article?) {
        if (article == null) {
            loadDataWithBaseURL("", "", TEXT_HTML, UTF8, null)
        } else {
            var contentText =
                if (article.mobilizedContent.isNotBlank() && article.mobilizedContent.isNotEmpty()) article.mobilizedContent else article.resume!!
            contentText = contentText.replace(HTML_IMG_REGEX.toRegex(), "")
            Log.i("text" , contentText)
            settings.blockNetworkImage = true

            val subtitle = StringBuilder(article.getReadablePublicationDate(context))
            if (article.author.isNotEmpty()) {
                subtitle.append(" &mdash; ").append(article.author)
            }

            val html = StringBuilder(CSS)
                .append(BODY_START)
                .append(TITLE_START).append(article.uri).append(TITLE_MIDDLE).append(article.title)
                .append(TITLE_END)
                .append(SUBTITLE_START).append(subtitle).append(SUBTITLE_END)
                .append(contentText)
                .append(BODY_END)
                .toString()

            // do not put 'null' to the base url...
            loadDataWithBaseURL("", html, TEXT_HTML, UTF8, null)

            // display top of article
            ObjectAnimator.ofInt(this@ArticleReadingView, "scrollY", scrollY, 0).setDuration(500)
                .start()
        }
    }

    private fun colorString(resourceInt: Int): String {
        val color = context.colorAttr(resourceInt)
        return String.format("#%06X", 0xFFFFFF and color)
    }

}