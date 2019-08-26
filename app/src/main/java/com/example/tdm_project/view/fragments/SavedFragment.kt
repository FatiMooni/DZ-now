
package com.example.tdm_project.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdm_project.R
import com.example.tdm_project.model.Article
import com.example.tdm_project.view.adapters.savedAdapter
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.viewmodel.ArticleViewModel


class SavedFragment : Fragment() {

    lateinit var rootView : View
    lateinit var savedAdapter : savedAdapter
    lateinit var rv : RecyclerView
    var newsList = ArrayList<ArticleViewModel>()


    companion object {
        fun getInstance() = SavedFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.saved_fragment, container, false)

        newsList = SharedSavedNews.getListSavedPosts()
        intialiserVertically()

        return rootView
    }



    private fun intialiserVertically() {
        rv = rootView.findViewById(R.id.recyler_view_saved_post)
        val layout = LinearLayoutManager(rootView.context)
        layout.orientation = RecyclerView.VERTICAL
        rv.layoutManager = layout
        savedAdapter = savedAdapter(rootView.context,newsList)
        rv.adapter = savedAdapter
    }

}