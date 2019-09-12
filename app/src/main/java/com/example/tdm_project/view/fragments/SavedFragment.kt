
package com.example.tdm_project.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
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
import com.example.tdm_project.services.FetchArticlesService
import com.example.tdm_project.view.activities.ArticleReadingActivity
import com.example.tdm_project.view.activities.ArticleReadingView
import com.example.tdm_project.view.activities.WebBrowserActivity
import com.example.tdm_project.view.adapters.ArticleVAdapter
import com.example.tdm_project.view.adapters.CustomMenuItem
import com.example.tdm_project.view.adapters.SavedMenuItem
import com.example.tdm_project.view.interfaces.ItemClicksListener
import com.example.tdm_project.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.horiz_news_view.view.*
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

                val popup = PopupMenu(view.context, view.menu_button)
                val inflater = popup.menuInflater
                inflater.inflate(R.menu.signet_menu, popup.menu)
                popup.setOnMenuItemClickListener(
                    SavedMenuItem(article, view.context)
                )
                popup.show()
            }

            override fun onItemClick(article: ArticleViewModel, position: Int) {
                val articl = Article(_id = article._id!!, title = article.title, resume = article.resume,
                    author = article.author, uri = article.uri , mobilizedContent = article.mobilizedContent!!)
                Log.i("content fetched" , article.title +
                        article.mobilizedContent)

                val intent = Intent(context, ArticleReadingActivity::class.java)
                intent.putExtra("article", articl)
                Toast.makeText(context,article.resume, Toast.LENGTH_LONG).show()
                context!!.startActivity(intent)
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