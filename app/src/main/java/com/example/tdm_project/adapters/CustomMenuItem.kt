package com.example.tdm_project.adapters


import android.content.Context
import androidx.appcompat.widget.PopupMenu
import android.view.MenuItem
import com.example.tdm_project.R
import com.example.tdm_project.data.SharedSavedNews
import com.example.tdm_project.data.news


class CustomMenuItem (private val it : news, private val cont : Context) : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {

                R.id.btn_share -> {
                    SharedSavedNews.sharePost(it,cont)
                    return true
                }

                R.id.btn_share_profile -> {
                    SharedSavedNews.shareProfilePost(it, cont)
                    return true
                }
                R.id.btn_save -> {
                    SharedSavedNews.savePost(it,cont)
                    return true
                }

            }
            return false
        }


    }
