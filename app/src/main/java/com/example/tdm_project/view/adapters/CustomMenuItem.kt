package com.example.tdm_project.view.adapters


import android.content.Context
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import com.example.tdm_project.R
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.viewmodel.ArticleViewModel


class CustomMenuItem(private val it: ArticleViewModel, private val cont: Context) : PopupMenu.OnMenuItemClickListener {

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            R.id.btn_share -> {
                SharedSavedNews.sharePost(it, cont)
                return true
            }

            R.id.btn_sharesms -> {
                return true
            }
            R.id.btn_save -> {
                SharedSavedNews.savePost(it, cont)
                return true
            }

        }
        return false
    }



}
