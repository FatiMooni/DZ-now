package com.example.tdm_project.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm_project.R
import com.example.tdm_project.databinding.ArticleBinding
import com.example.tdm_project.model.Article
import com.example.tdm_project.view.interfaces.ItemClicksListener
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.squareup.picasso.Picasso



class ArticleRVAdapter(val context: Context) :
    PagedListAdapter<Article, ArticleRVAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var itemListener: ItemClicksListener? = null

    companion object {

        @JvmField
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {

            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem._id == newItem._id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem._id == newItem._id &&
                        oldItem.isSavedOffline == newItem.isSavedOffline
            // TODO(do we need to check other elements !! )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ArticleBinding = DataBindingUtil.inflate(inflater, R.layout.horiz_news_view, parent, false)

        return ViewHolder(binding, itemListener!!)
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val newsContent = getItem(p1)
        p0.bind(newsContent)
    }


    inner class ViewHolder(var viewBinder: ArticleBinding, var listener: ItemClicksListener) :
        RecyclerView.ViewHolder(viewBinder.root) {


        fun bind(item: Article?) {

            this.viewBinder.item = item
            viewBinder.executePendingBindings()


            viewBinder.root.setOnClickListener {
                listener.onItemClick(ArticleViewModel(item!!), adapterPosition)
            }

            viewBinder.menuButton.setOnClickListener {
                listener.onPopupRequested(viewBinder.root, ArticleViewModel(item!!), adapterPosition)
            }

            viewBinder.saveButton.setOnClickListener {
                Log.i("saving", "u clicked on me , i am ${item!!.isSavedOffline}")
                listener.onSaveArticleClick(item,adapterPosition)
            }

        }


    }

    fun setOnItemListener(listener: ItemClicksListener) {
        this.itemListener = listener
    }
}