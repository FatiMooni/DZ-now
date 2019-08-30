
package com.example.tdm_project.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.view.adapters.savedAdapter
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.services.App
import com.example.tdm_project.view.adapters.ArticleVAdapter
import com.example.tdm_project.view.interfaces.ItemClicksListener
import com.example.tdm_project.viewmodel.ArticleViewModel
import org.jetbrains.anko.doAsync


class SavedFragment : Fragment() {

    lateinit var rootView : View
    lateinit var savedAdapter : ArticleVAdapter
    lateinit var rv : RecyclerView
    var newsList : LiveData<PagedList<Article>>? = null


    companion object {
        fun getInstance() = SavedFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.saved_fragment, container, false)

        intialiserVertically()

        savedAdapter.setOnItemListener(object : ItemClicksListener{
            override fun onPopupRequested(view: View, article: ArticleViewModel, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemClick(article: ArticleViewModel, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSaveArticleClick(article: Article, position: Int) {
                article.isSavedOffline = false
                doAsync {
                    App.db.articleDao().markArticleAsUnsaved(articleId = article._id)
                }
            }

        })

        newsList = LivePagedListBuilder(App.db.articleDao().getSavedArticles(),10).build()

        newsList!!.observe(this , Observer {
            savedAdapter.submitList(it)
        })

        return rootView
    }



    private fun intialiserVertically() {
        rv = rootView.findViewById(R.id.recyler_view_saved_post)
        val layout = LinearLayoutManager(rootView.context)
        layout.orientation = RecyclerView.VERTICAL
        rv.layoutManager = layout
        savedAdapter = ArticleVAdapter(context!!)
        rv.adapter = savedAdapter
    }

}