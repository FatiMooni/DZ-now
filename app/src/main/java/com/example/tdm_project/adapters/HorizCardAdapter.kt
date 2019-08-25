package com.example.tdm_project.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.tdm_project.R
import com.example.tdm_project.data.SharedSavedNews
import com.example.tdm_project.data.news
import com.example.tdm_project.databinding.ArticleBinding
import com.example.tdm_project.model.Article
import com.example.tdm_project.viewmodel.ArticleViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.horiz_news_view.view.*


class HorizCardAdapter(val context: Context, val news : ArrayList<ArticleViewModel>) : RecyclerView.Adapter<HorizCardAdapter.ViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ArticleBinding>(inflater,R.layout.horiz_news_view,parent,false)
        return ViewHolder(binding)
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

    inner class ViewHolder (var viewBinder : ArticleBinding) : RecyclerView.ViewHolder(viewBinder.root){
        lateinit var btnMenu : AppCompatImageButton

          fun bind(item : ArticleViewModel){
              this.viewBinder.item = item
              viewBinder.executePendingBindings()
          }
          /**    with(objet){
                  news_title.text = item.title
                  news_date.text = item.date.plus(", ")
                  news_descrp.text = item.resume
                  news_writer.text = item.author
                  Picasso
                      .get() // give it the context
                      .load(item.img)
                      .into(news_image)
              }


              btnMenu = objet.findViewById(R.id.menu_button)
              btnMenu.setOnClickListener {
                  showPopupMenu(btnMenu,item,context)
              }




           /*  objet.setOnClickListener {
                  SharedSavedNews.readArticle(item,context)
              }
*/


          }
        fun showPopupMenu(view: View, item: Article , context: Context) {
            // inflate menu
            val popup = PopupMenu(view.context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.card_menu, popup.menu)
            //popup.setOnMenuItemClickListener(CustomMenuItem(item,context))
            popup.show()
        }





**/
    }


}