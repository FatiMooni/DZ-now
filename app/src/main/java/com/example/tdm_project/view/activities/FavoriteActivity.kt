package com.example.tdm_project.view.activities

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm_project.R
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.NewsPaper
import com.example.tdm_project.model.Topic
import com.example.tdm_project.services.App
import com.example.tdm_project.services.App.Companion.context
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import com.example.tdm_project.view.adapters.NewsPaperAdapter
import com.example.tdm_project.viewmodel.CategoryViewModel
import com.example.tdm_project.viewmodel.NewsPaperViewModel
import org.jetbrains.anko.colorAttr
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.textColor

class FavoriteActivity : CustomBaseActivity() {
    lateinit var myPreference: PreferencesProvider
    lateinit var pref: PreferencesProvider
    var topics : ArrayList<Topic>? = null
    var newsList = ArrayList<NewsPaper>()
    val newstoinsert = ArrayList<NewsPaperViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get the view
        setContentView(R.layout.favorite_np)

        //for the shared preferences
        pref = PreferencesProvider(this)

        //init the adapter
        val recyclerView : RecyclerView = findViewById(R.id.news_paper_holder)
        val adapter = NewsPaperAdapter(this,             newstoinsert
        )
        val manager =  GridLayoutManager(this, 2)
        recyclerView.layoutManager = manager
        //recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter



        //observ the list
        val newsVModel = ViewModelProviders.of(this).get(NewsPaperViewModel::class.java)
        newsVModel.getNewpapers().observe(this , Observer {
             newsList = pref.loadNewsPaperList()
             Log.i("list",newsList.toString())
             it.forEach { el ->
                 if(newsList.contains(NewsPaper(el.img,el.title))){
                         el.isPrefered = true
                     }

                 Log.i("item" , el.title + el.isPrefered)
             }
             adapter.updateList(it)
        })

        //load the data
        newsVModel.loadNewsPaper()

        //
        adapter.setOnItemListener(object  : NewsPaperAdapter.ItemClickHandler{
            override fun onSetNewsPrefered(np: NewsPaperViewModel, position: Int) {
                //on doit l'ajouter au shared pref
                if (np.isPrefered){
                    newsList.add(NewsPaper(np.img,np.title))
                    pref.setNewsList(newsList)
                    Log.i("item pref" , "$position")

                }else {
                    Log.i("item not pref" , "$position")
                    newsList.remove(NewsPaper(np.img,np.title))
                    pref.setNewsList(newsList)
                }
                adapter.notifyItemChanged(position)

            }

        })


        //load topics
        var categoryList : List<Category>? = null


        val catVModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)

        catVModel.getCategories().observe(this, Observer {
            if (it != null && it.isNotEmpty()){
                categoryList = it
                //set topics list
                topics = pref.loadTopicsList(categoryList!!)
                initializeTopicsList(categoryList)

            }
        })
        doAsync {
            catVModel.getData()
        }





    }

    private fun initializeTopicsList(categoryList : List<Category>?) {
        if (categoryList != null && categoryList!!.isNotEmpty()) {
            val list = Topic.getTopics(categoryList!!)
            var layout = findViewById<LinearLayoutCompat>(R.id.topics_choice_holder)
            list.forEach {
                val check = CheckBox(this)
                check.text = this.getString(it.displayedTitle)

                if (topics!!.contains(it)) {
                    check.isChecked = true
                }
                check.setOnClickListener { v ->
                    val checked = (v as CheckBox).isChecked
                    if (checked) {
                        topics!!.add(it)
                        pref.setTopicsList(topics!!)
                    } else {
                        topics!!.remove(it)
                        pref.setTopicsList(topics!!)

                    }
                }
                layout.addView(check)
            }
        }
    }
}