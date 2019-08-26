@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.example.tdm_project.model

import com.example.tdm_project.R

data class Topic(
    var displayedTitle : Int,
    var title : String ,
    var IconLink : Int
)
fun getTopics() : ArrayList<Topic>{

    val topicsList = ArrayList<Topic>()
    topicsList.add(
        Topic(
            R.string.politics,
            title = "politics",
            IconLink = R.drawable.newspaper
        )
    )
    topicsList.add(Topic(R.string.tech, "tech", IconLink = R.drawable.tech_icon))
    topicsList.add(Topic(R.string.art, "art", R.drawable.art_icon))
    topicsList.add(Topic(R.string.art, "social", R.drawable.art_icon))
    topicsList.add(Topic(R.string.science, "science", R.drawable.art_icon))
    topicsList.add(Topic(R.string.sport, "sport", R.drawable.art_icon))
    topicsList.add(Topic(R.string.economics, "economics", R.drawable.art_icon))
    topicsList.add(Topic(R.string.culture, "culture", R.drawable.art_icon))



    return  topicsList
}