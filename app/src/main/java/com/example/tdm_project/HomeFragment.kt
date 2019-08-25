package com.example.tdm_project

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm_project.adapters.HorizCardAdapter
import com.example.tdm_project.adapters.vertCardAdapter
import com.example.tdm_project.data.Topic
import com.example.tdm_project.data.getList
import com.example.tdm_project.data.news
import com.example.tdm_project.model.Article
import com.example.tdm_project.services.ArticleService
import com.example.tdm_project.services.ServiceBuilder
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.example.tdm_project.viewmodel.ArticleViewModelFactory
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    lateinit var rootView: View
    lateinit var customHAdapter: HorizCardAdapter
    lateinit var customVAdapter: vertCardAdapter
    lateinit var rv: androidx.recyclerview.widget.RecyclerView
    lateinit var pref: PreferencesProvider

    var newsList = ArrayList<ArticleViewModel>()
    var selectedList = ArrayList<news>()
    var topicsList = ArrayList<Topic>()
    var choice: Int = 1


    //viewmodel
    private lateinit var vmodel : ArticleViewModel


    companion object {
        fun getInstance() = HomeFragment()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //set the view
        rootView = inflater.inflate(R.layout.home_fragment, container, false)
        pref = PreferencesProvider(rootView.context)

        //prepare the topics on the top of the fragment
        GetTopics()


        //set list of news
        //newsList = getList()
        //getData()
        intialiserHorizontally()


        vmodel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        vmodel.getArticles().observe(this, Observer {
             customHAdapter.swapData(it)
        })

        vmodel.getData()

        val btnHoriz = rootView.findViewById<ImageButton>(R.id.btn_horizt_display)
        btnHoriz.setOnClickListener {
            intialiserHorizontally()
            choice = 1
        }

        val btnVert = rootView.findViewById<ImageButton>(R.id.btn_vert_display)
        btnVert.setOnClickListener {
            //intialiserVertically()
            choice = 2
        }

        val btnAll = rootView.findViewById<AppCompatButton>(R.id.all_topics_btn)
        btnAll.setOnClickListener {
           // newsList = getList()
            when (choice) {
                1 -> intialiserHorizontally()
                //2 -> //intialiserVertically()
            }
        }


        return rootView
    }

   /* private fun intialiserVertically() {
        rv = rootView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyler_view_news)
        val layout = androidx.recyclerview.widget.LinearLayoutManager(rootView.context)
        layout.orientation = RecyclerView.VERTICAL
        rv.layoutManager = layout
        customVAdapter = vertCardAdapter(rootView.context, newsList)
        rv.adapter = customVAdapter
    }*/

    private fun intialiserHorizontally() {
        rv = rootView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyler_view_news)

        val layout = androidx.recyclerview.widget.LinearLayoutManager(rootView.context)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layout
        customHAdapter = HorizCardAdapter(rootView.context, newsList)
        rv.adapter = customHAdapter
    }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun GetTopics() {
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
                //chargeNews(cat)
            }

            layout.addView(btn)
        }
    }

    /**private fun chargeNews(titre: String?) {
        selectedList.clear()
        newsList = getList()
        newsList.forEach {
            if (it.category == titre) selectedList.add(it)
        }
        newsList = selectedList
        updateListTopics(selectedList)
    }

    private fun updateListTopics(myNewList: ArrayList<news>) {
        when (choice) {
            1 -> customHAdapter.updateList(myNewList)
            2 -> customVAdapter.updateList(myNewList)
        }
    }**/

    private fun getData() {
        val service = ServiceBuilder.buildService(ArticleService::class.java)
        val request = service.loadAllArticles()

        request.enqueue(object : Callback<List<Article>>{
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                Log.e("article fetch", "couldnt get the articles", t)
            }

            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                if (response.isSuccessful){
                    Toast.makeText(context,response.body().toString(),Toast.LENGTH_LONG).show()
                    newsList = response.body() as ArrayList<ArticleViewModel>
                }
            }

        })
    }

}