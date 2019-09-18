package com.example.tdm_project.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.tdm_project.R
import com.example.tdm_project.databinding.NewspaperBinding
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.example.tdm_project.viewmodel.NewsPaperViewModel

class NewsPaperAdapter (val context: Context, val news : ArrayList<NewsPaperViewModel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<NewsPaperAdapter.ViewHolder> () {

    lateinit var itemListener : ItemClickHandler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: NewspaperBinding = DataBindingUtil.inflate(inflater, R.layout.newspaper, parent, false)

        return ViewHolder(binding, itemListener)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsContent = news[position]
        holder.bind(newsContent)
    }

    fun updateList(newlist: List<NewsPaperViewModel>) {
        news.clear()
        news.addAll(newlist)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(private val viewBinder: NewspaperBinding , val listener: ItemClickHandler) : androidx.recyclerview.widget.RecyclerView.ViewHolder(viewBinder.root){

        fun bind(newsp : NewsPaperViewModel){
            this.viewBinder.item = newsp
            viewBinder.executePendingBindings()

            viewBinder.root.setOnClickListener {
                newsp.isPrefered = !newsp.isPrefered
                listener.onSetNewsPrefered(newsp, adapterPosition)
            }
        }
    }

    interface ItemClickHandler{
        fun onSetNewsPrefered(np : NewsPaperViewModel , position: Int)
    }

    fun setOnItemListener(listener: ItemClickHandler) {
        this.itemListener = listener
    }
}