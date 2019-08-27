package com.example.tdm_project.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val _id: String,
    val title: String,
    val resume: String?,
    val theme: String,
    val author: String,
    val date: String,
    val uri: String,
    val img: String
) : Parcelable, Comparable<Article> {
    override fun compareTo(other: Article): Int {
        return if (other._id == _id) 0
        else -1
    }

}