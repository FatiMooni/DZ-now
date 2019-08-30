package com.example.tdm_project.services

import android.accounts.NetworkErrorException
import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.data.Feed
import com.example.tdm_project.model.toDbFormat
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
import java.util.*
import java.util.concurrent.TimeUnit


/*
** This class will be an instance of Intent Service cause it helps to hundle the asychrone tasks
** while working on other tasks
 */
class FetchArticlesService : IntentService(FetchArticlesService::class.java.simpleName) {

    val sharedPref = PreferencesProvider(App.context)


    companion object : AnkoLogger {
        //constants

        /* if i want to refresh the feeds links/infos for one or more categories */
        const val ACTION_REFRESH_FEEDS = "tdm_project.services.FetchArticlesService.refreshFeedsAction"
        /* if i want to refresh the list of categories from backend categories */
        const val ACTION_REFRESH_CATEGORIES = "tdm_project.services.FetchArticlesService.refreshFeedsAction"

        const val FROM_AUTO_REFRESH = "tdm_project.services.FetchArticlesService.FROM_AUTO_REFRESH"
        const val EXTRA_CATEGORY_ID = "tdm_project.services.FetchArticlesService.EXTRA_CATEGORY_ID"
        const val EXTRA_CATEGORY = "tdm_project.services.FetchArticlesService.EXTRA_CATEGORY"
        const val EXTRA_CATEGORIES = "tdm_project.services.FetchArticlesService.EXTRA_CATEGORIES"


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

    private  fun refreshCategories(list : List<Category> , action: String){
        for (category in list){
            //refresh categories
            refreshCategory(category , action)
        }

        sharedPref.setNotFirstUse()
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
                            val feedToSave = Feed(link = attrs.value, title = feed.title, categoryId = category._id)

                            /** third : save the feed **/
                            App.db.feedDao().insert(feedToSave)

                        } else {
                            val feed = Feed(link = attrs.value, fetchError = true, categoryId = category._id)
                            App.db.feedDao().insert(feed)
                            Log.e("feed ${attrs.key}", attrs.value )
                        }

                    }

                }

            }

            /*** When the user decide to refresh the feeds of the category **/
            //TODO("when refresh categories is demanded")
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

    private fun getBaseUrl(link: String): String {
        var baseUrl = link
        val index = link.indexOf('/', 8) // this also covers https://
        if (index > -1) {
            baseUrl = link.substring(0, index)
        }

        return baseUrl
    }

    //we only refresh articles if we are online
    //get articles for this feed / link
    private fun refreshArticles(category: Category, acceptMinDate: Long): Int {

        //the list we gonna fetch
        val articles = mutableListOf<Article>()
        // the list we gonna save later
        val articlesToInsert = mutableListOf<Article>()

        //first we get list of saved feed
        val feeds = App.db.feedDao().getCategoryFeeds(category._id)

        feeds.forEach {feed ->
            val previousFeedState = feed.copy()
            articles.clear()
            articlesToInsert.clear()

            try {
                //save instance of articles that belongs to this feed
                createCall(feed.link).execute().use { response ->
                    val input = SyndFeedInput()
                    val romeFeed = input.build(XmlReader(response.body()!!.byteStream()))
                    articles.addAll(romeFeed.entries.asSequence().filter { it.publishedDate?.time ?: Long.MAX_VALUE > acceptMinDate }.map {
                        it.toDbFormat(
                            feed , category
                        )
                    })
                    feed.update(romeFeed)

                }
            } catch (t: Throwable) {
                feed.fetchError = true
                Log.e("fetching entries", "link : ${feed.link}" , t)

            }

            if(previousFeedState != feed){
                App.db.feedDao().update(feed)
            }

            /** first step we remove articles that have been added in the db before **/
            val existingIds = App.db.articleDao().getIdsOfArticles(category._id , feed.id)
            articles.removeAll { it._id in existingIds }
            Log.i("first step","removed all exised articles for $feed")

            /** second step : TODO(do we need any other optimisation)**/

            var foundExisting = false

            /** third step we insert **/
            for (article in articles) {
                if (existingIds.contains(article._id)) {
                    foundExisting = true
                }

                if (!foundExisting) {
                    // we will try to insert only new articles
                    if (!existingIds.contains(article._id)) {

                        //we try to improve the HTML representation
                        article.title = article.title.replace("\n", " ").trim()
                        article.resume?.let { desc ->
                            // Improve the resume of the article or the description
                            val improvedContent = HtmlOptimizer.improveHtmlContent(desc, feed.link)
                            // Get images
                                val imagesList = HtmlOptimizer.getImageURLs(improvedContent)
                                 Log.i("images", "$imagesList")
                                if (imagesList.isNotEmpty()) {
                                    if (article.img == null) {
                                        article.img = HtmlOptimizer.getMainImageURL(imagesList)
                                    }
                                    //imgUrlsToDownload[entry.id] = imagesList
                                }else if (article.img == null) {
                                    article.img = HtmlOptimizer.getMainImageURL(improvedContent)
                                }

                            //article.resume = improvedContent
                            articlesToInsert.add(article)
                        }

                    }
                    else {
                        foundExisting = true
                    }
                }

                Log.i("article :  ${article.title}" , "${article.publicationDate} ${article.uri} ${article.fetchDate} ${article.img}")

            }

            // Insert everything
            App.db.articleDao().insert(*(articlesToInsert.toTypedArray()))

        }
        return articles.size
    }

    /** to get previous day **/
    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    }


    /** to get previous day **/
    fun getHoursAgo(hoursAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, -hoursAgo)

        return calendar.time
    }

    /** delete articles that have been fetched before certain  number of days ago **/
    fun deleteAllArticles(daysAgo : Int){
        App.db.articleDao().deleteAllArticles(getDaysAgo(daysAgo).time)
    }

    private val handler = Handler()
    public override fun onHandleIntent(intent: Intent?) {
        if (intent == null) { // No intent, we quit
            return
        }

        val isFromAutoRefresh = intent.getBooleanExtra(FROM_AUTO_REFRESH, false)

        // Connectivity issue, we quit
        if (!App.hasNetwork()!!) {
            if (ACTION_REFRESH_FEEDS == intent.action && !isFromAutoRefresh) {
                // Display a toast in that case
                handler.post { toast("error of network").show() }
            }
            return
        }

        //refresh articles for a given category
        if (intent.hasExtra(EXTRA_CATEGORY_ID)){
            val categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID)
            val category = App.db.categoryDao().getCategory(categoryId)

            //so get articles from now ?? or lets say from 36hours ago
            refreshArticles(category,getHoursAgo(36).time )
            Log.i("time" , getHoursAgo(36).toString())
        }

        //refresh one category
        if (intent.hasExtra(EXTRA_CATEGORY) && ACTION_REFRESH_FEEDS == intent.action!!) {

            val category = intent.getParcelableExtra<Category>(EXTRA_CATEGORY)
            refreshCategory(category, intent.action!!)
        }

        //refresh all categories
        if (intent.hasExtra(EXTRA_CATEGORIES) && ACTION_REFRESH_FEEDS == intent.action!!) {

            val category = intent.getParcelableArrayListExtra<Category>(EXTRA_CATEGORIES)
            refreshCategories(category, intent.action!!)
        }

        //refreshFeed(category,6000)
        // fetch(this, isFromAutoRefresh, intent.action!!, intent.getLongExtra(EXTRA_FEED_ID, 0L))
    }


}