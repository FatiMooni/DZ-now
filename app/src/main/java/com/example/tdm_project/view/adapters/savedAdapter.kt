package com.example.tdm_project.view.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatImageButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.model.data.news
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.squareup.picasso.Picasso


class savedAdapter(val context: Context, val news : ArrayList<ArticleViewModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<savedAdapter.ViewHolder> (){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val newsCard = LayoutInflater.from(context).inflate(R.layout.saved_news_view,p0,false)
        return ViewHolder(newsCard)
    }

    override fun getItemCount(): Int {
        return  news.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val newsContent = news[p1]
        p0.bind(newsContent)
    }
    fun updateList(newlist: List<ArticleViewModel>) {
        news.clear()
        news.addAll(newlist)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder (private val objet: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(objet){
        lateinit var btnShare : AppCompatImageButton
        lateinit var btnShareProfile : AppCompatImageButton
        init {

        }
        fun bind(item : ArticleViewModel){
            objet.findViewById<TextView>(R.id.news_title).text = item.title
            objet.findViewById<TextView>(R.id.news_date).text = item.publicationDate.toString() + " By"
            objet.findViewById<TextView>(R.id.news_descrp).text = item.resume
            objet.findViewById<TextView>(R.id.news_writer).text = item.author
            val img =objet.findViewById<ImageView>(R.id.news_image)
            Picasso
                .get() // give it the context
                .load(item.img)
                .into(img)

            btnShareProfile = objet.findViewById<AppCompatImageButton>(R.id.btn_share_profile)
            btnShare = objet.findViewById<AppCompatImageButton>(R.id.btn_share)
            btnShare.setOnClickListener {
                SharedSavedNews.sharePost(item,context)

            }

            btnShareProfile.setOnClickListener {

                SharedSavedNews.shareProfilePost(item,context)

            }
            /*objet.setOnClickListener {
             SharedSavedNews.readArticle(item,context)
            }*/
        }


    }
}