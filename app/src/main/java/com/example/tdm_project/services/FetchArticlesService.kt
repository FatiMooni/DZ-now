package com.example.tdm_project.services

import android.accounts.NetworkErrorException
import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.data.Entry
import com.example.tdm_project.model.data.Feed
import com.example.tdm_project.model.data.toDbFormat
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.view.activities.CustomBaseActivity.Companion.isOnline
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit


/*
** This class will be an instance of Intent Service cause it helps to hundle the asychrone tasks
** while working on other tasks
 */
class FetchArticlesService : IntentService(FetchArticlesService::class.java.simpleName) {

    val sharedPref = PreferencesProvider(App.context)


    companion object : AnkoLogger {
        //constants
        const val ACTION_REFRESH_FEEDS = "tdm_project.services.FetchArticlesService.refreshAction"
        const val FROM_AUTO_REFRESH = "tdm_project.services.FetchArticlesService.FROM_AUTO_REFRESH"
        const val EXTRA_CATEGORY_ID = "tdm_project.services.FetchArticlesService.EXTRA_CATEGORY_ID"


        //cookies manager
        private val COOKIE_MANAGER = CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }

        //okhttp client
        private val HTTP_CLIENT: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(COOKIE_MANAGER))
            .build()

        //create a url retriever
        fun createCall(url: String) = HTTP_CLIENT.newCall(
            Request.Builder()
                .url(url)
                .header(
                    "User-agent",
                    "Mozilla/5.0 (compatible) AppleWebKit Chrome Safari"
                ) // some feeds need this to work properly
                .addHeader("accept", "*/*")
                .build()
        )


    }

    /** this function is used to refresh one category **/
    private fun refreshCategory(category: Category, action : String = "") {
        when {
            /*** When it is the first time to use the app **/
            (App.isOnline && sharedPref.isFirstUse()) -> {
                //add category to the local database
                App.db.categoryDao().insert(category)

                //retrive all the links
                category.feeds.forEach { attrs ->
                    createCall(attrs.value).execute().use { response ->
                        val input = SyndFeedInput()
                        if (response.isSuccessful) {
                            //si le feed exist for real

                            /** first : get the syndicate feed **/
                            val feed = input.build(XmlReader(response.body()!!.byteStream()))
                            Log.i("feed ${attrs.key}", feed.title + feed.categories)

                            /** second : create the feed entity to save**/
                            val feedToSave = Feed(link = feed.link, title = feed.title, categoryId = category._id)

                            /** third : save the feed **/
                            App.db.feedDao().insert(feedToSave)

                        } else {
                            val feed = Feed(link = attrs.value, fetchError = true, categoryId = category._id)
                            App.db.feedDao().insert(feed)
                            Log.e("feed ${attrs.key}", feed.title , throw NetworkErrorException("couldnt find this"))
                        }


                    }

                }

                sharedPref.setNotFirstUse()
            }

            /*** When the user decide to refresh the feeds of the category **/
            (App.isOnline && ACTION_REFRESH_FEEDS == action) -> {
                //update the category in the local database in case of any added or deleted feed
                App.db.categoryDao().update(category)

                val existedFeeds = App.db.feedDao().getCategoryFeeds(category._id)

                //retrive all the links
                category.feeds.forEach { attrs ->
                    createCall(attrs.value).execute().use { response ->
                        val input = SyndFeedInput()
                        if (response.isSuccessful) {
                            //si le feed exist for real

                            /** first : get the syndicate feed **/
                            val feed = input.build(XmlReader(response.body()!!.byteStream()))
                            Log.i("feed ${attrs.key}", feed.title + feed.categories)

                            /** second : create the feed entity to save**/
                            val feedToSave = Feed(link = feed.link, title = feed.title, categoryId = category._id)

                            /** third : check if the feed exist **/
                            if(!existedFeeds.contains(feedToSave))

                            { /** fourth : save the new feed **/
                                App.db.feedDao().insert(feedToSave) }

                        }
                    }

                }

                //delete all feeds that doesnt exist anymore
                existedFeeds.forEach {
                    if(!category.feeds.containsValue(it.link)) App.db.feedDao().delete(it)
                }
            }

            /** Else we will just fetch the feeds from the localDataBase **/
        }
    }

    //get articles for this feed / link
    private fun refreshFeed(feed: Feed, acceptMinDate: Long): Int {
        val entries = mutableListOf<Entry>()
        /*val entriesToInsert = mutableListOf<Entry>()
        val imgUrlsToDownload = mutableMapOf<String, List<String>>()

        val previousFeedState = feed.copy()*/
        try {
            createCall(feed.link).execute().use { response ->
                val input = SyndFeedInput()
                val romeFeed = input.build(XmlReader(response.body()!!.byteStream()))
                entries.addAll(romeFeed.entries.asSequence().filter { it.publishedDate?.time ?: Long.MAX_VALUE > acceptMinDate }.map {
                    it.toDbFormat(
                        feed
                    )
                })
                Log.i("fetching entries", entries[1].toString())
                //category.update(romeFeed)
            }
        } catch (t: Throwable) {
            feed.fetchError = true
        }

        return 0
    }


    private val handler = Handler()
    public override fun onHandleIntent(intent: Intent?) {
        if (intent == null) { // No intent, we quit
            return
        }

        val isFromAutoRefresh = intent.getBooleanExtra(FROM_AUTO_REFRESH, false)

        // Connectivity issue, we quit
        if (!isOnline) {
            if (ACTION_REFRESH_FEEDS == intent.action && !isFromAutoRefresh) {
                // Display a toast in that case
                handler.post { toast("error of network").show() }
            }
            return
        }

        val category = intent.getParcelableExtra<Category>(EXTRA_CATEGORY_ID)
        refreshCategory(category, intent.action!!)
        //refreshFeed(category,6000)
        // fetch(this, isFromAutoRefresh, intent.action!!, intent.getLongExtra(EXTRA_FEED_ID, 0L))
    }


}