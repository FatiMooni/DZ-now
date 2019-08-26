
package com.example.tdm_project.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tdm_project.R
import com.example.tdm_project.view.adapters.savedAdapter
import com.example.tdm_project.model.data.SharedSavedNews
import com.example.tdm_project.model.data.news


class SavedFragment : Fragment() {

    lateinit var rootView : View
    lateinit var savedAdapter : savedAdapter
    lateinit var rv : androidx.recyclerview.widget.RecyclerView
    var newsList = ArrayList<news>()


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
        rv = rootView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyler_view_saved_post)
        val layout = androidx.recyclerview.widget.LinearLayoutManager(rootView.context)
        layout.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        rv.layoutManager = layout
        savedAdapter = savedAdapter(rootView.context,newsList)
        rv.adapter = savedAdapter
    }

}