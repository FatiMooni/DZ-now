package com.example.tdm_project.view.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.tdm_project.R
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.model.data.news
import com.squareup.picasso.Picasso

class vertCardAdapter(val context: Context, val news : ArrayList<news>) : androidx.recyclerview.widget.RecyclerView.Adapter<vertCardAdapter.ViewHolder> (){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val newsCard = LayoutInflater.from(context).inflate(R.layout.vert_news_view,p0,false)
        return ViewHolder(newsCard)
    }

    override fun getItemCount(): Int {
        return  news.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val newsContent = news[p1]
        p0.bind(newsContent)
    }
    fun updateList(newlist: List<news>) {
        news.clear()
        news.addAll(newlist)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder (private val objet: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(objet){

        lateinit var btnShare : AppCompatImageButton
        lateinit var btnShareProfile : AppCompatImageButton
        lateinit var btnSave : AppCompatImageButton
        init {

        }
          fun bind(item : news){
              objet.findViewById<TextView>(R.id.news_title).text = item.Title
              objet.findViewById<TextView>(R.id.news_date).text = item.Date +","
              objet.findViewById<TextView>(R.id.news_descrp).text = item.Second_title
              objet.findViewById<TextView>(R.id.news_writer).text = item.Writer
              val img =objet.findViewById<ImageView>(R.id.news_image)
              Log.i("IMAAAGE",item.Image)
              Picasso
                  .get() // give it the context
                  .load(item.Image)
                  .into(img)

              val btnMenu = objet.findViewById<AppCompatImageButton>(R.id.menu_button)
              btnMenu.setOnClickListener {
                  showPopupMenu(btnMenu,item,context)
              }


              objet.setOnClickListener {
                  SharedSavedNews.readArticle(item,context)
              }

          }
        fun showPopupMenu(view: View, item: news , context: Context) {
            // inflate menu
            val popup = PopupMenu(view.context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.card_menu, popup.menu)
            popup.setOnMenuItemClickListener(CustomMenuItem(item,context))
            popup.show()
        }


    }
}