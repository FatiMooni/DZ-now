package com.example.tdm_project.view.fragments
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.Topic
import com.example.tdm_project.model.data.Video
import com.example.tdm_project.services.App
import com.example.tdm_project.services.FetchArticlesService
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.view.activities.WebBrowserActivity
import com.example.tdm_project.view.adapters.ArticleRVAdapter
import com.example.tdm_project.view.adapters.ArticleVAdapter
import com.example.tdm_project.view.adapters.CustomMenuItem
import com.example.tdm_project.view.interfaces.ItemClicksListener
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.example.tdm_project.viewmodel.CategoryViewModel
import com.facebook.Profile
import kotlinx.android.synthetic.main.horiz_news_view.view.*
import org.apache.commons.io.FilenameUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jsoup.Connection
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.net.URLConnection
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment()  {

    private lateinit var rootView: View
    private lateinit var articleAdapter: PagedListAdapter<Article, *>
    private lateinit var articleVAdapter: ArticleVAdapter
    private lateinit var rv: RecyclerView
    private lateinit var pref: PreferencesProvider
    var userId=""
    private var articlesLiveData: LiveData<PagedList<Article>>? = null

    //viewmodel
    private lateinit var vmodel: ArticleViewModel

    private var newsList = ArrayList<ArticleViewModel>()
    private var topicsList = ArrayList<Topic>()


    companion object {
        var verticallayout: Boolean = false
        var ACTION_REFRESH_CATEGORIES_FROM_BACK: Boolean = false
        private const val CATEGORY_ARG = "category arg"

        @JvmStatic
        fun getHomeFragment(categoryId: String): HomeFragment {
            return HomeFragment().apply {
                categoryId.let {
                    arguments = bundleOf(CATEGORY_ARG to categoryId)
                }
            }
        }


    }

    var categoryId: String? = null
        set(value) {
            field = value
            initDataObservers()
        }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //set the view
        rootView = inflater.inflate(R.layout.home_fragment, container, false)
        pref = PreferencesProvider(rootView.context)


        /**first step fetch all existed articles from room**/
        //set list of news
        if (!verticallayout) {
            rvInitializer(LinearLayoutManager.HORIZONTAL)
            categoryId = null
            initDataObservers()
        }
        else rvInitializer(LinearLayoutManager.VERTICAL)


        vmodel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        vmodel.getArticles().observe(this, Observer {
            newsList = it
            if (verticallayout) (articleVAdapter)
            else (articleAdapter as ArticleRVAdapter)
        })

        vmodel.getData()


        val catVModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)

        catVModel.getCategories().observe(this, Observer {
            if (it.isNotEmpty() && it != null) {
                it.forEach { category ->
                    Log.i("link", category.feeds.toString())}

                    if(pref.isFirstUse()){
                        Log.i("first use" , "am gonna fetch feeds")
                        context?.startService(
                            Intent(context, FetchArticlesService::class.java)
                                .setAction(FetchArticlesService.ACTION_REFRESH_FEEDS)
                                .putParcelableArrayListExtra(FetchArticlesService.EXTRA_CATEGORIES, it))
                    }

                //prepare the topics on the top of the fragment
                getTopics(it)
            }
        })
        //get the categories == les != themes
        // if it was online and its first time or it was requested -> from back end
        // else from room database
        doAsync {
            catVModel.getData()
        }

        //get all saved articles
        val btnAll = rootView.findViewById<AppCompatButton>(R.id.all_topics_btn)
        btnAll.setOnClickListener {
            categoryId = null
            initDataObservers()
        }

        //Set the current User
        val userGreeting =  rootView.findViewById<AppCompatTextView>(R.id.user_greeting)
        val profile= Profile.getCurrentProfile()
        if (profile != null) {
            var greeting = userGreeting.text.toString()
            userGreeting.text = greeting + "" + profile.name.toString()
            userId=Profile.getCurrentProfile().id
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(savedInstanceState != null){
            toast("i have been saved")
        }
    }
    /** to initialize the recyclerview **/
    private fun rvInitializer(orientation: Int) {
        rv = rootView.findViewById(R.id.recyler_view_news)
        val layout = LinearLayoutManager(rootView.context)
        layout.orientation = orientation
        rv.layoutManager = layout
        when (orientation) {
            LinearLayoutManager.HORIZONTAL -> {
                articleAdapter =
                    ArticleRVAdapter(rootView.context)
                (articleAdapter as ArticleRVAdapter).setOnItemListener(object : ItemClicksListener {
                    override fun onSaveArticleClick(article: Article, position: Int) {
                        article.isSavedOffline = !article.isSavedOffline
                        doAsync {
                              if(article.isSavedOffline){
                                  App.db.articleDao().markArticleAsSaved(userId,article._id)
                                  context?.startService(
                                      Intent(context, FetchArticlesService::class.java)
                                          .setAction(FetchArticlesService.ACTION_ARTICLE)
                                          .putExtra("article",article) )
                              } else {
                                  vmodel.unsaveArticle(userId,article._id)
                                  App.db.articleDao().markArticleAsUnsaved(article._id)

                              }

                        }
                    }

                    override fun onPopupRequested(view: View, article: ArticleViewModel, position: Int) {

                        val popup = PopupMenu(view.context, view.menu_button)
                        val inflater = popup.menuInflater
                        inflater.inflate(R.menu.card_menu, popup.menu)
                        popup.setOnMenuItemClickListener(
                            CustomMenuItem(article, view.context)
                        )
                        popup.show()
                    }

                    override fun onItemClick(article: ArticleViewModel, position: Int) {
                        val intent = Intent(context, WebBrowserActivity::class.java)
                        intent.putExtra(WebBrowserActivity.EXTRA_URI, article.uri)
                        Toast.makeText(context,article.resume,Toast.LENGTH_LONG).show()
                        context!!.startActivity(intent)

                        /*** Extract & Saving Video in  Dao ***/
                        Log.i("VIDEO","BEFORE")

                         doAsync {
                             extractVideos(article)
                         }

                        Log.i("VIDEO","AFTER")






                    }

                })
            }
            LinearLayoutManager.VERTICAL -> {
                articleVAdapter =
                    ArticleVAdapter(rootView.context)
                (articleVAdapter).setOnItemListener(object : ItemClicksListener {
                    override fun onSaveArticleClick(article: Article, position: Int) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onPopupRequested(view: View, article: ArticleViewModel, position: Int) {

                        val popup = PopupMenu(view.context, view.menu_button)
                        val inflater = popup.menuInflater
                        inflater.inflate(R.menu.card_menu, popup.menu)
                        popup.setOnMenuItemClickListener(
                            CustomMenuItem(article, view.context)
                        )
                        popup.show()
                    }

                    override fun onItemClick(article: ArticleViewModel, position: Int) {
                        val intent = Intent(context, WebBrowserActivity::class.java)
                        intent.putExtra(WebBrowserActivity.EXTRA_URI, article.uri)
                        context!!.startActivity(intent)
                    }

                })

            }
        }

        rv.adapter = articleAdapter
    }

    /** to retrive the data in the pagedlist adapter**/
    //TODO(check when to call this)
    private fun initDataObservers() {
        articlesLiveData = LivePagedListBuilder(
            when {
                categoryId != null && categoryId!!.isNotBlank() -> App.db.articleDao().getArticlesOfCategory(
                    categoryId!!,
                    Date().time
                )
                else -> App.db.articleDao().getAllArticles(Date().time)
            }, 20
        ).build()

        articlesLiveData!!.observe(this, Observer { pagedList ->
            articleAdapter.submitList(pagedList)
            Log.i("articles in main ", "ma list ${pagedList.size}")
        })
    }


    /** to set the list of topics in the top of home fragments**/
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getTopics(fetchedCategories: List<Category>) {
        val layout = rootView.findViewById<LinearLayoutCompat>(R.id.Topics_buttom_holder)
        layout.weightSum = 100F
        var style = 0
        var num = 0

        topicsList = pref.loadTopicsList(fetchedCategories)
        topicsList.forEach {

            val btn = AppCompatButton(rootView.context)
            val draw = rootView.resources.getDrawable(it.IconLink, null)

            btn.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null)
            btn.setPadding(45, 0, 45, 0)
            btn.compoundDrawablePadding = 20
            btn.maxWidth = 700
            btn.minWidth = 350
            btn.minHeight = 200
            val cat = it.title
            val id = it.categoryId
            val titre = rootView.context.resources.getString(it.displayedTitle)
            btn.text = titre
            btn.setTextColor(rootView.resources.getColor(R.color.white, null))

            when (num) {
                0 -> style = R.drawable.style_blue
                1 -> style = R.drawable.style_red
                2 -> style = R.drawable.style_green
            }

            num = (num + 1) % 3

            btn.background = rootView.resources.getDrawable(style, null)

            btn.setOnClickListener {
                //chargeNews(cat)
                Log.i("the articles of $cat $id", "the category with $id")
                if (App.hasNetwork()!!)
                    doAsync {
                        context?.startService(
                        Intent(context, FetchArticlesService::class.java)
                            .setAction(FetchArticlesService.ACTION_REFRESH_FEEDS)
                            .putExtra(FetchArticlesService.EXTRA_CATEGORY_ID, id)
                          )
                    }
                    categoryId = id
                    initDataObservers()
            }
            layout.addView(btn)
        }
    }



  fun extractVideos ( article : ArticleViewModel)  {
      // link for test "https://www.ennaharonline.com/%d8%a8%d8%a7%d9%84%d9%81%d9%8a%d8%af%d9%8a%d9%88-%d9%88%d8%a7%d9%84%d8%b5%d9%88%d8%b1-%d8%a7%d9%84%d8%ac%d8%b2%d8%a7%d8%a6%d8%b1%d9%8a%d9%88%d9%86-%d9%8a%d8%ae%d8%b1%d8%ac%d9%88%d9%86-%d9%84%d9%84/"
      val conn = Jsoup.connect(article.uri).method(Connection.Method.GET)
      val resp = conn.execute()
        val html = resp.body()
        val doc = Jsoup.parse(html, "UTF-8")
      val hrefs = doc.getElementsByTag("iframe")
      for (el in hrefs) {
          val link = el.attr("src")
          if (link == "" || link != null) {
              if ((link!!.contains("http") || link!!.contains("https")) &&  ((link.contains("://youtu.be/") || link.contains("youtube")))  ) {
                  val basename = FilenameUtils.getBaseName(link)
                  val extension = FilenameUtils.getExtension(link)
                  val filename = basename + "." + extension
                  Log.i("VIDEOLINK",link)
                  val id = link.substring(30,41)
                  Log.i("VIDEOID",id)
                  var linkYout = "https://youtube.com/watch?v="+id
                  Log.i("VIDEOYOUT",linkYout)
                  var youTubeExtractor = object : YouTubeExtractor(this@HomeFragment.context!!) {

                      override fun onExtractionComplete(ytFiles: SparseArray<YtFile>, vMeta: VideoMeta) {
                          if (ytFiles != null) {
                              Log.i("VIDEO", "INSIDEfUN1")
                              var itag=22
                              var downloadUrl = ytFiles.get(itag).getUrl()
                              var img = "https://cdn1.imggmi.com/uploads/2019/9/18/2d42c776e9b5c2f882ee9ee2033402e4-full.png"
                              if (article.img != null)  img= article.img!!

                              var  video =Video (videoTitle = article.title, videoUri = downloadUrl,thumbnail = img)
                              Log.i("VIDEOCONVLINK0",video.videoUri)
                              doAsync {
                                  App.db.videoDao().insert(video)
                              }

                              Log.i("VIDEOCONVLINK0","Inserted")


                              //videos.add(video)

                          }
                      }
                  }.extract(linkYout, true, true)


                   // You probably would want to keep one of these extractors around.
        /*var extractor : YouTubeExtractor  = YouTubeExtractor.create()

        extractor.extract(id).enqueue(object : Callback<YouTubeExtractionResult> {
                      override fun onResponse(call: Call<YouTubeExtractionResult>, response: Response<YouTubeExtractionResult>) {
                          Log.i("EXTRACT", "Saving User : call enqueue")
                          if (response.isSuccessful) {
                              var result: YouTubeExtractionResult = response.body()!!
                              var link = result.getHd1080VideoUri()
                              bindVideoResult(response.body())
                              Log.i("EXTRACTlink",link.toString())
                          }


                      }
                      override fun onFailure(call: Call<YouTubeExtractionResult>, t: Throwable) {
                          Log.i("EXTRACT", "error CODE:" + t.message)
                      }
                  })*/


              }
          }
      }



   // return videos
    }


    /** Not used in Extract videos **/
/*

    val elements = doc.getElementsByAttributeValue("type", "video/mp4")
        for (el in elements) {
            val link = el.attr("src")
            if (link == "" || link != null) {
                if (link!!.contains("http") || link!!.contains("https")) {
                    val basename = FilenameUtils.getBaseName(link)
                    val extension = FilenameUtils.getExtension(link)
                    val filename = basename + "." + extension
                    Log.i("VIDEOTYPE",link)
                    //videos.add(Video(filename,,,))
                }
            }
        }
        val tags = doc.getElementsByTag("video")
        for (el in tags) {
            val link = el.attr("src")
            if (link == "" || link != null) {
                if (link!!.contains("http") || link!!.contains("https")) {
                    val basename = FilenameUtils.getBaseName(link)
                    val extension = FilenameUtils.getExtension(link)
                    val filename = basename + "." + extension
                    Log.i("VIDEOTAG",link)
                    //videos.add(Video(link, filename))
                }
            }
        }

  */
}





