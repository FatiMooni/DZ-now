package com.example.tdm_project.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm_project.R
import com.example.tdm_project.view.adapters.ArticleRVAdapter
import com.example.tdm_project.model.Topic
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.viewmodel.ArticleViewModel


class HomeFragment : Fragment() {

    lateinit var rootView: View
    lateinit var articleAdapter: ArticleRVAdapter
    lateinit var rv: RecyclerView
    lateinit var pref: PreferencesProvider
    //viewmodel
    private lateinit var vmodel: ArticleViewModel

    var newsList = ArrayList<ArticleViewModel>()
    var topicsList = ArrayList<Topic>()


    companion object {
        var verticallayout: Boolean = false
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //set the view
        rootView = inflater.inflate(R.layout.home_fragment, container, false)
        pref = PreferencesProvider(rootView.context)

        //prepare the topics on the top of the fragment
        getTopics()


        //set list of news
        if (!verticallayout) rvInitialiser(LinearLayoutManager.HORIZONTAL)
        else rvInitialiser(LinearLayoutManager.VERTICAL)


        vmodel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        vmodel.getArticles().observe(this, Observer {

            newsList = it
            articleAdapter.swapData(it)
        })

        vmodel.getData()

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
            LinearLayoutManager.HORIZONTAL -> articleAdapter =
                ArticleRVAdapter(rootView.context, newsList, ArticleRVAdapter.LAYOUT_HORIZ)
            LinearLayoutManager.VERTICAL -> articleAdapter =
                ArticleRVAdapter(rootView.context, newsList, ArticleRVAdapter.LAYOUT_VERT)
        }
        rv.adapter = articleAdapter
    }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getTopics() {
        var layout = rootView.findViewById<LinearLayoutCompat>(R.id.Topics_buttom_holder)
        var style = 0
        var num = 0

        topicsList = pref.loadTopicsList()
        topicsList.forEach {

            var btn = AppCompatButton(rootView.context)
            val draw = rootView.resources.getDrawable(it.IconLink, null)

            btn.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null)
            btn.setPadding(45, 0, 45, 0)
            btn.compoundDrawablePadding = 20
            btn.maxWidth = 450
            btn.minWidth = 350
            btn.minHeight = 200
            val cat = it.title
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
            }

            layout.addView(btn)
        }
    }

    private fun chargeNews(titre: String?) {
        val selectedList = ArrayList<ArticleViewModel>()

        newsList.forEach {
            if (it.theme == titre) selectedList.add(it)
        }
        articleAdapter.swapData(selectedList)
    }

}


