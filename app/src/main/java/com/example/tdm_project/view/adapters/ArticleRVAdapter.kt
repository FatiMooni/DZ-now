package com.example.tdm_project.view.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import com.example.tdm_project.R
import com.example.tdm_project.databinding.ArticleBinding
import com.example.tdm_project.model.Article
import com.example.tdm_project.view.interfaces.ItemClicksListener
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.squareup.picasso.Picasso


class ArticleRVAdapter(val context: Context, val news : ArrayList<ArticleViewModel>) : RecyclerView.Adapter<ArticleRVAdapter.ViewHolder> (){

    private var itemListener : ItemClicksListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ArticleBinding =  DataBindingUtil.inflate(inflater, R.layout.horiz_news_view, parent, false)

            return ViewHolder(binding,itemListener!!)
    }

    override fun getItemCount(): Int {
        return  news.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val newsContent = news[p1]
        p0.bind(newsContent)
    }
    fun swapData(newlist: List<ArticleViewModel>) {
        news.clear()
        news.addAll(newlist)
        this.notifyDataSetChanged()
    }


    inner class ViewHolder (var viewBinder : ArticleBinding , var listener: ItemClicksListener) : RecyclerView.ViewHolder(viewBinder.root){



          fun bind(item : ArticleViewModel){
              this.viewBinder.item = item
              viewBinder.executePendingBindings()
              if(item.img.isNotBlank() && item.img.isNotEmpty())
              { Picasso
                  .get() // give it the context
                  .load(item.img)
                  .into(viewBinder.newsImage)}

              viewBinder.root.setOnClickListener {
                  listener.onItemClick(item , adapterPosition)
              }

              viewBinder.menuButton.setOnClickListener {
                  listener.onPopupRequested(viewBinder.root,item, adapterPosition)
              }

          }





    }

    fun setOnItemListener(listener: ItemClicksListener){
        this.itemListener = listener
    }
}