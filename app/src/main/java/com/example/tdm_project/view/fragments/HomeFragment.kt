package com.example.tdm_project.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
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
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.Topic
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
import kotlinx.android.synthetic.main.horiz_news_view.view.*
import org.jetbrains.anko.doAsync
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    lateinit var rootView: View
    lateinit var articleAdapter: PagedListAdapter<Article,*>
    lateinit var articleVAdapter: ArticleVAdapter
    lateinit var rv: RecyclerView
    lateinit var pref: PreferencesProvider
    private var articlesLiveData: LiveData<PagedList<Article>>? = null

    //viewmodel
    private lateinit var vmodel: ArticleViewModel

    var newsList = ArrayList<ArticleViewModel>()
    var topicsList = ArrayList<Topic>()


    companion object {
        var verticallayout: Boolean = false
        var ACTION_REFRESH_CATEGORIES_FROM_BACK : Boolean = false
        var CATEGORY_ARG = "category arg"

        fun getHomeFragment(categoryId : String) : HomeFragment
        {
            return HomeFragment().apply{
                categoryId.let {
                    arguments = bundleOf(CATEGORY_ARG to categoryId )
                }
            }
        }




    }

    var categoryId : String? = null
        set(value) {
            field = value
            InitDataObservers()
        }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //set the view
        rootView = inflater.inflate(R.layout.home_fragment, container, false)
        pref = PreferencesProvider(rootView.context)




        //set list of news
        if (!verticallayout) rvInitialiser(LinearLayoutManager.HORIZONTAL)
        else rvInitialiser(LinearLayoutManager.VERTICAL)


        vmodel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        vmodel.getArticles().observe(this, Observer {

            newsList = it
            if (verticallayout) (articleAdapter as ArticleVAdapter).swapData(it)
            else (articleAdapter as ArticleRVAdapter)
        })

        vmodel.getData()



        var catVModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)

        catVModel.getCategories().observe(this, Observer {
            if (it.isNotEmpty() && it != null) {
                it.forEach { category ->
                    Log.i("link", category.feeds.toString())

                    context?.startService(
                        Intent(context, FetchArticlesService::class.java)
                            .setAction(FetchArticlesService.ACTION_REFRESH_CATEGORIES)
                            .putExtra(FetchArticlesService.EXTRA_CATEGORY, category)
                    )

                }

                //prepare the topics on the top of the fragment
                getTopics(it)
            }

        })

        doAsync {
            catVModel.getData()
        }

        val btnHoriz = rootView.findViewById<ImageButton>(R.id.btn_horizt_display)
        btnHoriz.setOnClickListener {
            rvInitialiser(LinearLayoutManager.HORIZONTAL)
            verticallayout = false
        }

        val btnVert = rootView.findViewById<ImageButton>(R.id.btn_vert_display)
        btnVert.setOnClickListener {
            rvInitialiser(LinearLayoutManager.VERTICAL)
            verticallayout = true
        }

        val btnAll = rootView.findViewById<AppCompatButton>(R.id.all_topics_btn)
        btnAll.setOnClickListener {
            when (verticallayout) {
                true -> rvInitialiser(LinearLayoutManager.VERTICAL)
                false -> rvInitialiser(LinearLayoutManager.HORIZONTAL)

            }
        }

        return rootView
    }


    private fun rvInitialiser(orientation: Int) {
        rv = rootView.findViewById(R.id.recyler_view_news)
        val layout = LinearLayoutManager(rootView.context)
        layout.orientation = orientation
        rv.layoutManager = layout
        when (orientation) {
            LinearLayoutManager.HORIZONTAL -> {
                articleAdapter =
                    ArticleRVAdapter(rootView.context)
                (articleAdapter as ArticleRVAdapter).setOnItemListener(object : ItemClicksListener {
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
            LinearLayoutManager.VERTICAL -> {
                articleVAdapter =
                    ArticleVAdapter(rootView.context, newsList)
                (articleAdapter as ArticleVAdapter).setOnItemListener(object : ItemClicksListener {
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

    private fun InitDataObservers(){
        articlesLiveData = LivePagedListBuilder(when{
            categoryId != null && categoryId!!.isNotBlank() -> App.db.articleDao().getArticlesOfCategory(categoryId!!, Date().time)
            else -> App.db.articleDao().getAllArticles(Date().time)
        },30).build()

        articlesLiveData!!.observe(this , Observer { pagedList ->
            articleAdapter.submitList(pagedList)
            Log.i("articles in main " , "ma list $pagedList")
        })
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getTopics(fetchedCategories: List<Category>) {
        var layout = rootView.findViewById<LinearLayoutCompat>(R.id.Topics_buttom_holder)
        layout.weightSum = 100F
        var style = 0
        var num = 0

        topicsList = pref.loadTopicsList(fetchedCategories)
        topicsList.forEach {

            var btn = AppCompatButton(rootView.context)
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
                chargeNews(cat)
                Log.i("the articles of $cat", "the category with $id")
               if(App.hasNetwork()!!)
                context?.startService(
                    Intent(context, FetchArticlesService::class.java)
                        .setAction(FetchArticlesService.ACTION_REFRESH_FEEDS)
                        .putExtra(FetchArticlesService.EXTRA_CATEGORY_ID, id)
                )
                else {
                        categoryId = id
                       InitDataObservers()
               }

            }

            layout.addView(btn)
        }
    }

    private fun chargeNews(titre: String?) {
        val selectedList = ArrayList<ArticleViewModel>()

        newsList.forEach {
            if (it.categoryOrigin == titre) selectedList.add(it)
        }
        if (verticallayout) (articleAdapter as ArticleVAdapter).swapData(selectedList)
        else (articleAdapter as ArticleRVAdapter)
    }

}


