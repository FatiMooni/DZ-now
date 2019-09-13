package com.example.tdm_project.model

data class NewsPaper(    var img : String = "",
                         var title : String = "") : Comparable<NewsPaper> {
    override fun compareTo(other: NewsPaper): Int {
        return if(this.title == other.title) 1
        else -1
    }
}