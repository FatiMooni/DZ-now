
package com.example.tdm_project.view.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.ArticlePost
import com.example.tdm_project.services.App
import com.example.tdm_project.services.ArticleService
import com.example.tdm_project.services.ServiceBuilder
import com.example.tdm_project.view.activities.ArticleReadingActivity
import com.example.tdm_project.view.adapters.ArticleVAdapter
import com.example.tdm_project.view.adapters.SavedMenuItem
import com.example.tdm_project.view.interfaces.ItemClicksListener
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.facebook.Profile
import kotlinx.android.synthetic.main.horiz_news_view.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk21.listeners.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SavedFragment : Fragment() {

    lateinit var rootView : View
    lateinit var savedAdapter : ArticleVAdapter
    lateinit var rv : RecyclerView
    lateinit var syncBtn : Button
    var userId =""
    var newsList : LiveData<PagedList<Article>>? = null
    private lateinit var vmodel: ArticleViewModel


    companion object {
        fun getInstance() = SavedFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.saved_fragment, container, false)
        syncBtn = rootView.findViewById(R.id.btn_sync)
        intialiserVertically()
        userId = Profile.getCurrentProfile().id
        vmodel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        savedAdapter.setOnItemListener(object : ItemClicksListener{
            override fun onPopupRequested(view: View, article: ArticleViewModel, position: Int) {

                val popup = PopupMenu(view.context, view.menu_button)
                @Suppress("NAME_SHADOWING") val inflater: MenuInflater = popup.menuInflater
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
                vmodel.unsaveArticle(userId,article._id)

                doAsync {
                    App.db.articleDao().markArticleAsUnsaved(articleId = article._id)
                }
            }

        })

        newsList = LivePagedListBuilder(App.db.articleDao().getSavedArticles(userId),10).build()

        newsList!!.observe(this , Observer {
            savedAdapter.submitList(it)
        })

        syncBtn.setOnClickListener {

            vmodel.getSavedArticle(userId)

        }




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